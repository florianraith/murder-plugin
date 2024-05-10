package com.florianraith.murder;

import com.florianraith.murder.command.*;
import com.florianraith.murder.config.Messages;
import com.florianraith.murder.phase.LobbyPhase;
import com.florianraith.murder.phase.WorldPhase;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;

@Getter
public class MurderPlugin extends JavaPlugin {

    private Injector injector;
    private WorldPhase currentPhase;
    private World gameWorld;

    private DisguiseManager disguiseManager;

    @Override
    public void onEnable() {
        Messages messages = new Messages();
        messages.setConfig(loadConfig("messages.yml"));

        MurderPluginModule module = new MurderPluginModule(this, messages);
        injector = Guice.createInjector(module);
        injector.injectMembers(this);

        registerCommand(SwitchPhaseCommand.class);
        registerCommand(CountdownCommand.class);
        registerCommand(DisplayMessageCommand.class);
        registerCommand(ToggleDisguiseCommand.class);

        registerEvents(WorldListener.class);

        setPhase(new LobbyPhase());

        disguiseManager = injector.getInstance(DisguiseManager.class);
        disguiseManager.enable();
    }

    @Override
    public void onDisable() {
        if (currentPhase != null) {
            currentPhase.onDisable();
        }

        disguiseManager.disable();
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

    public FileConfiguration loadConfig(String name) {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, true);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    private void registerEvents(Class<? extends Listener> listenerClass) {
        Bukkit.getPluginManager().registerEvents(injector.getInstance(listenerClass), this);
    }

    private void registerCommand(Class<? extends CommandExecutor> commandClass) {
        try {
            Field nameField = commandClass.getField("NAME");
            String name = (String) nameField.get(null);

            PluginCommand command = Objects.requireNonNull(getCommand(name));
            CommandExecutor executor = injector.getInstance(commandClass);

            command.setExecutor(executor);

            if (executor instanceof TabCompleter completer) {
                command.setTabCompleter(completer);
            }

            getLogger().info("Registered /" + name + " command");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(commandClass.getSimpleName() + " must have a public static String NAME field");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("The NAME field of " + commandClass.getSimpleName() + " is not accessible");
        }
    }

}