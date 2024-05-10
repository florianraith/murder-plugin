package com.florianraith.murder.command;

import com.florianraith.murder.config.Messages;
import com.florianraith.murder.MurderPlugin;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplayMessageCommand implements CommandExecutor, TabCompleter {

    public static final String NAME = "message";

    @Inject private MurderPlugin plugin;
    @Inject private Messages messages;

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            messages.setConfig(plugin.loadConfig("messages.yml"));
            sender.sendMessage(messages.prefix("Reloaded messages"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            new File(plugin.getDataFolder(), "messages.yml").delete();
            messages.setConfig(plugin.loadConfig("messages.yml"));
            sender.sendMessage(messages.prefix("Reset messages"));
            return true;
        }

        sender.sendMessage(messages.prefixed(args[0]));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], messages.getKeys(), completions);
        Collections.sort(completions);
        return completions;
    }

}
