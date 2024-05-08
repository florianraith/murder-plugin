package com.florianraith.murder.item;

import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.phase.GamePhase;
import com.florianraith.murder.util.Events;
import com.florianraith.murder.util.Items;
import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StartGameItem implements ItemHandler {

    @Inject private MurderPlugin plugin;

    @Override
    public ItemStack getItem() {
        return Items.create(Material.CLOCK, ChatColor.GOLD + "Start Game");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!checkItem(event) || !Events.isRightClick(event)) {
            return;
        }

        event.setCancelled(true);
        plugin.setPhase(new GamePhase());
    }

}
