package com.florianraith.murder;

import com.florianraith.murder.command.CommandExecutor;
import com.florianraith.murder.command.CountdownCommand;
import com.florianraith.murder.command.SwitchPhaseCommand;
import com.florianraith.murder.phase.LobbyPhase;
import com.florianraith.murder.phase.WorldPhase;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.lang.reflect.Field;
import java.util.Objects;

@Plugin(name = "Murder", version = "1.0")
@Author("Florian Raith")
@ApiVersion(ApiVersion.Target.v1_20)
@Getter
public class MurderPlugin extends JavaPlugin {

    private Injector injector;
    private WorldPhase currentPhase;
    private World gameWorld;

    @Override
    public void onEnable() {
        MurderPluginModule module = new MurderPluginModule(this);
        injector = Guice.createInjector(module);
        injector.injectMembers(this);

        registerCommand(SwitchPhaseCommand.class);
        registerCommand(CountdownCommand.class);

        registerEvents(WorldListener.class);

        setPhase(new LobbyPhase());
    }

    @Override
    public void onDisable() {
        if (currentPhase != null) {
            currentPhase.onDisable();
        }
    }

    public void setPhase(WorldPhase phase) {
        injector.injectMembers(phase);

        if (currentPhase != null) {
            currentPhase.onDisable();
        }

        currentPhase = phase;
        Bukkit.getOnlinePlayers().forEach(phase::preparePlayer);
        phase.onEnable();
    }

    public World getGameWorld() {
        if (gameWorld == null) {
            gameWorld = new WorldCreator("clue").createWorld();
        }
        return gameWorld;
    }

    private void registerEvents(Class<? extends Listener> listenerClass) {
        Bukkit.getPluginManager().registerEvents(injector.getInstance(listenerClass), this);
    }

    private void registerCommand(Class<? extends CommandExecutor> commandClass) {
        try {
            Field nameField = commandClass.getField("NAME");
            String name = (String) nameField.get(null);

            Objects.requireNonNull(getCommand(name)).setExecutor(injector.getInstance(commandClass));
            getLogger().info("Registered /" + name + " command");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(commandClass.getSimpleName() + " must have a public static String NAME field");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("The NAME field of " + commandClass.getSimpleName() + " is not accessible");
        }
    }

}