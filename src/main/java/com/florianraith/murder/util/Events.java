package com.florianraith.murder.util;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class Events {

    private Events() {
        throw new UnsupportedOperationException();
    }

    public static boolean isRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

}
