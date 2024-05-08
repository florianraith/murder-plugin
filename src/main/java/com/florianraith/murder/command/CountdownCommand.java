package com.florianraith.murder.command;

import com.florianraith.murder.Countdown;
import com.florianraith.murder.Countdownable;
import com.florianraith.murder.MurderPlugin;
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
        if (!(plugin.getCurrentPhase() instanceof Countdownable countdownable)) {
            sender.sendMessage("This phase doesn't have a countdown");
            return false;
        }

        Countdown countdown = countdownable.getCountdown();

        if (args.length > 0) {
            long seconds;

            try {
                seconds = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("The first argument has to be a number");
                return false;
            }

            if (seconds < 0) {
                sender.sendMessage("The number has to be positive");
                return false;
            }

            countdown.stop();
            countdown.start(seconds);
            sender.sendMessage("Countdown started with " + seconds + " seconds");
            return false;
        }


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
