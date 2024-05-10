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

    /**
     * @param previous previous phase; null if this is the first phase
     */
    void onEnable(WorldPhase previous);

    /**
     * @param next next phase; null if this is the last phase
     */
    void onDisable(WorldPhase next);

    void onJoin(Player player);

    void onQuit(Player player);

}
