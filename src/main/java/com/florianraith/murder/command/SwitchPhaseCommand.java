package com.florianraith.murder.command;

import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.config.Messages;
import com.florianraith.murder.phase.*;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

@Commands(@Command(name = SwitchPhaseCommand.NAME))
public class SwitchPhaseCommand implements CommandExecutor {

    public static final String NAME = "phase";

    @Inject private MurderPlugin plugin;
    @Inject private Messages messages;

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(messages.prefix("Phase must be on of ['lobby', 'preparing', 'game', 'end']"));
            return false;
        }

        WorldPhase phase = switch (args[0]) {
            case "lobby" -> new LobbyPhase();
            case "preparing" -> new PreparingPhase();
            case "game" -> new GamePhase();
            case "end" -> new EndPhase();
            default -> null;
        };

        if (phase == null) {
            sender.sendMessage(messages.prefix("Phase must be on of ['lobby', 'preparing', 'game', 'end']"));
            return false;
        }

        plugin.setPhase(phase);
        return false;
    }
}
