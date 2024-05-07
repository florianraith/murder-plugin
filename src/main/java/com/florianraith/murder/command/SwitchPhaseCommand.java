package com.florianraith.murder.command;

import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.phase.*;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

@Commands(@Command(name = SwitchPhaseCommand.NAME))
public class SwitchPhaseCommand implements CommandExecutor {

    public static final String NAME = "phase";

    @Inject private MurderPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Phase must be on of ['lobby', 'preparing', 'game', 'end']");
            return false;
        }

        Class<? extends WorldPhase> phase = switch (args[0]) {
            case "lobby" -> LobbyPhase.class;
            case "preparing" -> PreparingPhase.class;
            case "game" -> GamePhase.class;
            case "end" -> EndPhase.class;
            default -> null;
        };

        if (phase == null) {
            sender.sendMessage("Phase must be on of ['lobby', 'preparing', 'game', 'end']");
            return false;
        }

        plugin.setPhase(phase);
        return false;
    }
}
