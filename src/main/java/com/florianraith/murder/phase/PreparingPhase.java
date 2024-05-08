package com.florianraith.murder.phase;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.util.Attributes;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class PreparingPhase implements WorldPhase {

    @Inject private MurderPlugin plugin;
    @Inject private World world;

    private Countdown countdown;

    @Override
    public void onEnable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.teleport(world.getSpawnLocation()));

        countdown = new Countdown(plugin, () -> plugin.setPhase(new GamePhase()), 15);
        countdown.setMessage("The preparing phase ends in %s");

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("Prepare for the game");
        Bukkit.broadcastMessage("");

        countdown.start();
    }

    @Override
    public void onDisable() {
        countdown.stop();
    }

    @Override
    public void onJoin(Player player) {
        player.teleport(world.getSpawnLocation());
    }

    @Override
    public void onQuit(Player player) {
        if (Bukkit.getOnlinePlayers().size() - 1 < LobbyPhase.MIN_PLAYERS) {
            countdown.stop();
            Bukkit.broadcastMessage("Not enough players to start the game.");
            Bukkit.broadcastMessage("Returning to lobby...");

            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.setPhase(new LobbyPhase()), 20);
        }
    }

    @Override
    public void preparePlayer(Player player) {
        WorldPhase.super.preparePlayer(player);
        player.setHealth(Attributes.set(player, Attribute.GENERIC_MAX_HEALTH, 2));
    }

}
