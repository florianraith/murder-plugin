package com.florianraith.murder.phase;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.Objects;

public interface WorldPhase {

    default void preparePlayer(Player player) {
        player.getInventory().clear();
        player.setInvulnerable(true);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);

        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(maxHealthAttribute);
        player.setHealth(maxHealthAttribute.getBaseValue());
    }

    void onEnable();

    void onDisable();

    void onJoin(Player player);

    void onQuit(Player player);

}
