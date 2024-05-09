package com.florianraith.murder.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Alternative TabCompleter without the command parameter as it is rarely used.
 * By using this interface instead you avoid the name collision with the @Command annotation.
 */
public interface TabCompleter extends org.bukkit.command.TabCompleter {

    @Override
    default @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return onTabComplete(sender, label, args);
    }

    List<String> onTabComplete(CommandSender sender, String label, String[] args);
}
