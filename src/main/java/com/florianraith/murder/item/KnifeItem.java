package com.florianraith.murder.item;

import com.florianraith.murder.PlayerRole;
import com.florianraith.murder.phase.GamePhase;
import com.florianraith.murder.util.Items;
import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class KnifeItem implements ItemHandler {

    @Inject private GamePhase game;
    @Inject private World world;

    @Override
    public ItemStack getItem() {
        return Items.create(Material.GOLDEN_SWORD, ChatColor.GOLD + "Knife");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)
                || !(event.getEntity() instanceof Player target)
                || !checkItem(damager.getInventory().getItemInMainHand())) {
            return;
        }

        if (game.hasRole(target, PlayerRole.BYSTANDER)) {
            world.playSound(target.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
            game.assignSpectator(target);
            game.checkEnd();
        }
    }

}
