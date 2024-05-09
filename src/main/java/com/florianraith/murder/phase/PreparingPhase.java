package com.florianraith.murder.phase;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.CountdownFactory;
import com.florianraith.murder.Countdownable;
import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.config.Messages;
import com.florianraith.murder.util.Attributes;
import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class PreparingPhase implements WorldPhase, Countdownable {

    @Inject private MurderPlugin plugin;
    @Inject private World world;
    @Inject private Messages messages;
    @Inject private CountdownFactory countdownFactory;

    @Getter private Countdown countdown;

    @Override
    public void onEnable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.teleport(world.getSpawnLocation()));
        Bukkit.broadcast(messages.prefixed("preparing.info"));

        countdown = countdownFactory.phase(GamePhase::new, 15);
        countdown.setMessage("preparing.countdown");
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
            Bukkit.broadcast(messages.prefixed("preparing.not_enough_players"));
            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.setPhase(new LobbyPhase()), 20);
        }
    }

    @Override
    public void preparePlayer(Player player) {
        WorldPhase.super.preparePlayer(player);
        player.setHealth(Attributes.set(player, Attribute.GENERIC_MAX_HEALTH, 2));
    }

}
