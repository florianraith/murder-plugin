package com.florianraith.murder.util;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

public final class Worlds {

    private Worlds() {
        throw new UnsupportedOperationException();
    }

    public static void ensurePlayerIsInWorld(Player player, World world) {
        if (!player.getWorld().equals(world)) {
            player.teleport(world.getSpawnLocation());
        }
    }

    public static void ensurePlayersAreInWorld(Collection<Player> players, World world) {
        players.forEach(player -> ensurePlayerIsInWorld(player, world));
    }

}
