package com.florianraith.murder.phase;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.MurderPlugin;
import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LobbyPhase implements WorldPhase {

    public static final int MIN_PLAYERS = 2;

    @Inject private MurderPlugin plugin;
    private final World world;
    @Getter private Countdown countdown;

    public LobbyPhase() {
        this.world = Bukkit.getWorld("lobby");
    }

    @Override
    public void onEnable() {
        countdown = new Countdown(plugin, () -> plugin.setPhase(PreparingPhase.class), 15);
        countdown.setMessage("The game starts in %s");
        Bukkit.getOnlinePlayers().forEach(this::onJoin);
    }

    @Override
    public void onDisable() {
        countdown.stop();
    }

    @Override
    public void onJoin(Player player) {
        player.teleport(world.getSpawnLocation());

        if (Bukkit.getOnlinePlayers().size() >= MIN_PLAYERS && countdown.isStopped()) {
            countdown.start();
        }
    }

    @Override
    public void onQuit(Player player) {
        if ((Bukkit.getOnlinePlayers().size() - 1) < MIN_PLAYERS && countdown.isRunning()) {
            countdown.stop();
            Bukkit.broadcastMessage("Not enough players to start the game.");
        }
    }

}
