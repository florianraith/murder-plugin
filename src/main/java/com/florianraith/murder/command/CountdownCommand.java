package com.florianraith.murder.command;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.state.LobbyPhase;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

@Commands(@Command(name = CountdownCommand.NAME))
public class CountdownCommand implements CommandExecutor {

    @Inject MurderPlugin plugin;
    public static final String NAME = "countdown";

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(plugin.getCurrentWorldPhase() instanceof LobbyPhase lobby)) {
            sender.sendMessage("The countdown is only accessible during lobby phase");
            return false;
        }

        Countdown countdown = lobby.getCountdown();

        if (countdown.isRunning()) {
            countdown.stop();
            sender.sendMessage("Countdown stopped");
        } else {
            countdown.start();
            sender.sendMessage("Countdown started");
        }

        return false;
    }
}
