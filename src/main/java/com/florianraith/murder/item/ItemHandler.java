package com.florianraith.murder.item;

import com.florianraith.murder.util.Items;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface ItemHandler extends Listener {

    ItemStack getItem();

    default boolean checkItem(ItemStack item) {
        return Items.isAssignedTo(item, this.getClass());
    }

    default boolean checkItem(PlayerInteractEvent event) {
        return checkItem(event.getItem());
    }

}
