package com.florianraith.murder;

import com.florianraith.murder.config.Messages;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class Countdown {

    @Inject private JavaPlugin plugin;
    @Inject private Messages messages;

    private final Runnable runnable;
    private final long seconds;

    private BukkitTask task;
    private long countdown;

    @Setter private String message = "countdown";

    public void start() {
        start(this.seconds);
    }

    public void start(long seconds) {
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
                String verb = messages.raw("vars.seconds." + (countdown == 1 ? "sg" : "pl"));

                Bukkit.broadcast(messages.prefixed(
                        message,
                        Placeholder.unparsed("time", String.valueOf(countdown)),
                        Placeholder.unparsed("verb", verb)
                ));
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
