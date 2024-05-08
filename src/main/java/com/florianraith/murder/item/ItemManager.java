package com.florianraith.murder.item;

import com.florianraith.murder.util.Items;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    @Inject private JavaPlugin plugin;
    @Inject private Injector injector;

    private final Map<Class<? extends ItemHandler>, ItemHandler> handlers = new HashMap<>();

    public void register(Class<? extends ItemHandler> itemClass) {
        if (handlers.containsKey(itemClass)) {
            throw new IllegalStateException("The item " + itemClass.getSimpleName() + " is already registered");
        }

        ItemHandler handler = injector.getInstance(itemClass);

        Bukkit.getPluginManager().registerEvents(handler, plugin);
        handlers.put(itemClass, handler);
    }

    public void unregisterAll() {
        handlers.values().forEach(HandlerList::unregisterAll);
        handlers.clear();
    }

    public ItemStack get(Class<? extends ItemHandler> itemClass) {
        ItemStack item = getHandler(itemClass).getItem();
        Items.assign(item, itemClass);
        return item;
    }

    public ItemHandler getHandler(Class<? extends ItemHandler> itemClass) {
        if (!handlers.containsKey(itemClass)) {
            throw new IllegalStateException("The item " + itemClass.getSimpleName() + " is not registered");
        }

        return handlers.get(itemClass);
    }

}
