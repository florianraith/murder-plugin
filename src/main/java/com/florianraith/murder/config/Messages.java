package com.florianraith.murder.config;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@Setter
public class Messages {

    private FileConfiguration config;

    private Component parse(String key, boolean prefixed, TagResolver.Single[] resolvers) {
        String prefix = prefixed ? config.getString("prefix", "") : "";

        List<String> lines = null;
        if (config.isString(key)) {
            lines = new ArrayList<>(List.of(config.getString(key, key)));
        }
        if (config.isList(key)) {
            lines = config.getStringList(key);
        }

        if (lines == null || lines.isEmpty()) {
            return deserialize(prefix, new TagResolver.Single[]{}).append(Component.text(key));
        }

        Component component = deserialize(prefix + lines.removeFirst(), resolvers);
        for (String line : lines) {
            component = component.appendNewline().append(deserialize(prefix + line, resolvers));
        }

        return component;
    }

    public List<String> getKeys() {
        return new ArrayList<>(config.getKeys(true));
    }

    public Component prefix(String text) {
        return deserialize(config.getString("prefix", "") + text, new TagResolver.Single[]{});
    }

    public Component prefixed(String key, TagResolver.Single... resolvers) {
        return parse(key, true, resolvers);
    }

    public Component get(String key, TagResolver.Single... resolvers) {
        return parse(key, false, resolvers);
    }

    public String raw(String key) {
        return config.getString(key, key);
    }

    private Component deserialize(String text, TagResolver.Single[] resolvers) {
        return MiniMessage.miniMessage().deserialize(text, resolvers);
    }

}
