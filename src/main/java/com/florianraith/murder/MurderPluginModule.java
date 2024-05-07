package com.florianraith.murder;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class MurderPluginModule extends AbstractModule {

    private final MurderPlugin plugin;

    @Override
    protected void configure() {
        bind(MurderPlugin.class).toInstance(plugin);
        bind(JavaPlugin.class).toInstance(plugin);
        bind(World.class).toProvider(plugin::getGameWorld);
    }
}
