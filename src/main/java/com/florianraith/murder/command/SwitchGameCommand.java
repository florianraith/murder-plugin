package com.florianraith.murder.command;

import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.state.PreGamePhase;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

@Commands(@Command(name = SwitchGameCommand.NAME, aliases = {"s-game"}))
public class SwitchGameCommand implements CommandExecutor {

    public static final String NAME = "switch-to-game";

    @Inject private MurderPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        plugin.switchWorldState(PreGamePhase.class);
        return true;
    }

}
