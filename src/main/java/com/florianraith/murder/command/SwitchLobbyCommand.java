package com.florianraith.murder.command;

import com.florianraith.murder.MurderPlugin;
import com.florianraith.murder.state.LobbyPhase;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

@Commands(@Command(name = SwitchLobbyCommand.NAME, aliases = {"s-lobby"}))
public class SwitchLobbyCommand implements CommandExecutor {

    public static final String NAME = "switch-to-lobby";

    @Inject private MurderPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        plugin.switchWorldState(LobbyPhase.class);
        return false;
    }

}
