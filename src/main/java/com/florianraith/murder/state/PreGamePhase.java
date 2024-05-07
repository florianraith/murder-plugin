package com.florianraith.murder.state;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.MurderPlugin;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PreGamePhase extends WorldPhase {

    @Inject private MurderPlugin plugin;
    @Inject private World world;

    private Countdown countdown;

    @Override
    public void onEnable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.teleport(world.getSpawnLocation()));
        countdown = new Countdown(plugin, () -> plugin.switchWorldState(GamePhase.class), 15);

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("Prepare for the game");
        Bukkit.broadcastMessage("");

        countdown.start();
        countdown.setMessage("The preparing phase ends in %s");
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

            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.switchWorldState(LobbyPhase.class), 20);
        }
    }

}
