package com.florianraith.murder.phase;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.CountdownFactory;
import com.florianraith.murder.Countdownable;
import com.florianraith.murder.MurderPlugin;
import com.google.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EndPhase implements WorldPhase, Countdownable {

    private final List<Player> spectators;

    @Inject private MurderPlugin plugin;
    @Inject private World world;
    @Inject private CountdownFactory countdownFactory;

    @Getter private Countdown countdown;

    public EndPhase() {
        this.spectators = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        countdown = countdownFactory.phase(LobbyPhase::new, 9);
        countdown.setMessage("end.countdown");
        countdown.start();

        spectators.forEach(spectator -> spectator.setGameMode(GameMode.SPECTATOR));
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
