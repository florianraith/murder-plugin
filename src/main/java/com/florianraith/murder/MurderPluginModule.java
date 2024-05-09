package com.florianraith.murder;

import com.florianraith.murder.config.Messages;
import com.florianraith.murder.phase.*;
import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class MurderPluginModule extends AbstractModule {

    private final MurderPlugin plugin;
    private final Messages messages;

    @Override
    protected void configure() {
        bind(MurderPlugin.class).toInstance(plugin);
        bind(JavaPlugin.class).toInstance(plugin);
        bind(Messages.class).toInstance(messages);
        bind(World.class).toProvider(plugin::getGameWorld);
        bindPhase(LobbyPhase.class);
        bindPhase(PreparingPhase.class);
        bindPhase(GamePhase.class);
        bindPhase(EndPhase.class);
    }

    private <T extends WorldPhase> void bindPhase(Class<T> phaseClass) {
        bind(phaseClass).toProvider(() -> {
            WorldPhase currentPhase = plugin.getCurrentPhase();

            if (!currentPhase.getClass().isAssignableFrom(phaseClass)) {
                throw new IllegalStateException("The current phase " + currentPhase.getClass().getSimpleName() + " is not a " + phaseClass.getSimpleName());
            }

            return phaseClass.cast(currentPhase);
        });
    }
}
