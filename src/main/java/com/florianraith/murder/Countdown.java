package com.florianraith.murder;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class Countdown {

    private final JavaPlugin plugin;
    private final Runnable runnable;
    private final long seconds;

    private BukkitTask task;
    private long countdown;

    @Setter private String message = "Countdown ends in %s";

    public void start() {
        if (isRunning()) {
            throw new IllegalStateException("The countdown is already running");
        }

        countdown = seconds;

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (countdown == 0) {
                runnable.run();
                return;
            }

            if (countdown % 5 == 0 || countdown <= 5) {
                String verb = countdown == 1 ? " second" : " seconds";

                Bukkit.broadcastMessage(String.format(message, countdown + verb));
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1));
            }

            countdown--;
        }, 0, 20);
    }


    public void stop() {
        if (isRunning()) {
            task.cancel();
            task = null;
        }
    }

    public boolean isStopped() {
        return task == null;
    }

    public boolean isRunning() {
        return !isStopped();
    }
}
