package com.florianraith.murder.phase;

import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class WorldPhase {

    private static final int MAX_FOOD_LEVEL = 20;

    public void preparePlayer(Player player) {
        player.getInventory().clear();
        player.setInvulnerable(true);
        player.setFoodLevel(MAX_FOOD_LEVEL);
        player.setGameMode(GameMode.ADVENTURE);

        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(maxHealthAttribute);
        player.setHealth(maxHealthAttribute.getBaseValue());
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onJoin(Player player);

    public abstract void onQuit(Player player);

}
