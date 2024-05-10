package com.florianraith.murder;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BossBars {

    private final Map<Player, BossBar> bossBars = new HashMap<>();

    public BossBar create(Component message, BossBar.Color color) {
        return BossBar.bossBar(message, 1, color, BossBar.Overlay.PROGRESS);
    }

    public void show(Player player, BossBar bossBar) {
        clear(player);
        player.showBossBar(bossBar);
        bossBars.put(player, bossBar);
    }

    public void clear(Player player) {
        if (bossBars.containsKey(player)) {
            player.hideBossBar(bossBars.get(player));
            bossBars.remove(player);
        }
    }

    public void clearAll() {
        for (Player player : bossBars.keySet()) {
            player.hideBossBar(bossBars.get(player));
        }
    }

}
