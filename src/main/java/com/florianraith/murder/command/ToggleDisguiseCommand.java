package com.florianraith.murder.command;

import com.florianraith.murder.DisguiseManager;
import com.florianraith.murder.MurderPlugin;
import com.google.inject.Inject;
import dev.iiahmed.disguise.Disguise;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

@Commands(@Command(name = ToggleDisguiseCommand.NAME))
public class ToggleDisguiseCommand implements CommandExecutor {

    public static final String NAME = "disguise";

    @Inject private DisguiseManager disguiseManager;
    private boolean toggled = false;

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {

        if (!toggled) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                disguiseManager.requestDisguise(player, "D_" + player.getName());
            }
        } else {
            disguiseManager.resetAll();
        }

        toggled = !toggled;


//        Disguise disguise = Disguise.builder()
//                .setName("D_" + sender.getName())
//                .setSkin("Steve")
//                .build();
//
//        dev.iiahmed.disguise.DisguiseManager.getProvider().disguise((Player) sender, disguise);

        return false;
    }
}
