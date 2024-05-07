package com.florianraith.murder.phase;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.MurderPlugin;
import com.google.inject.Inject;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class EndPhase implements WorldPhase {

    @Inject private MurderPlugin plugin;
    @Inject private World world;

    private Countdown countdown;

    @Override
    public void onEnable() {
        countdown = new Countdown(plugin, () -> plugin.setPhase(LobbyPhase.class), 5);
        countdown.setMessage("A new round starts in %s");
        countdown.start();
    }

    @Override
    public void onDisable() {
        countdown.stop();
    }

    @Override
    public void onJoin(Player player) {
    }

    @Override
    public void onQuit(Player player) {
    }
}
