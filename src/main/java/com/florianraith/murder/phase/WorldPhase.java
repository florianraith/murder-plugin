package com.florianraith.murder.phase;

import com.florianraith.murder.util.Attributes;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public interface WorldPhase {

    default void preparePlayer(Player player) {
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(Attributes.reset(player, Attribute.GENERIC_MAX_HEALTH));
    }

    void onEnable();

    void onDisable();

    void onJoin(Player player);

    void onQuit(Player player);

}
