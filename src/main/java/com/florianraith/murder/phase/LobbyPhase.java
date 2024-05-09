package com.florianraith.murder.phase;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.CountdownFactory;
import com.florianraith.murder.Countdownable;
import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.config.Messages;
import com.florianraith.murder.item.ItemManager;
import com.florianraith.murder.item.StartGameItem;
import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LobbyPhase implements WorldPhase, Countdownable {

    public static final int MIN_PLAYERS = 3;

    @Inject private MurderPlugin plugin;
    @Inject private ItemManager itemManager;
    @Inject private Messages messages;
    @Inject private CountdownFactory countdownFactory;

    private final World world;
    @Getter private Countdown countdown;

    public LobbyPhase() {
        this.world = Bukkit.getWorld("lobby");
    }

    @Override
    public void onEnable() {
        itemManager.register(StartGameItem.class);

        countdown = countdownFactory.phase(PreparingPhase::new, 15);
        countdown.setMessage("lobby.countdown");

        Bukkit.getOnlinePlayers().forEach(this::onJoin);
    }

    @Override
    public void onDisable() {
        itemManager.unregisterAll();
        countdown.stop();
    }

    @Override
    public void onJoin(Player player) {
        player.teleport(world.getSpawnLocation());

        if (Bukkit.getOnlinePlayers().size() >= MIN_PLAYERS && countdown.isStopped()) {
            countdown.start();
        }

        player.getInventory().addItem(itemManager.get(StartGameItem.class));
    }

    @Override
    public void onQuit(Player player) {
        if ((Bukkit.getOnlinePlayers().size() - 1) < MIN_PLAYERS && countdown.isRunning()) {
            countdown.stop();
            Bukkit.broadcast(messages.prefixed("lobby.not_enough_players"));
        }
    }

}
