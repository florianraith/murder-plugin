package com.florianraith.murder.command;

import com.florianraith.murder.DisguiseManager;
import com.florianraith.murder.MurderPlugin;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleDisguiseCommand implements CommandExecutor {

    public static final String NAME = "disguise";

    @Inject private DisguiseManager disguiseManager;
    private boolean toggled = false;

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {

        if (args.length > 0) {
            disguiseManager.requestDisguise((Player) sender, args[0]);
            return true;
        }

        if (!toggled) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                disguiseManager.requestDisguise(player, "D_" + player.getName());
            }
        } else {
            disguiseManager.resetAll();
        }

        toggled = !toggled;

        return true;
    }
}
