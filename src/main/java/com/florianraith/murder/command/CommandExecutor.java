package com.florianraith.murder.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Alternative CommandExecutor without the command parameter as it is rarely used.
 * By using this interface instead you avoid the name collision with the @Command annotation.
 */
public interface CommandExecutor extends org.bukkit.command.CommandExecutor {
    default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return onCommand(sender, label, args);
    }

    boolean onCommand(CommandSender sender, String label, String[] args);
}
