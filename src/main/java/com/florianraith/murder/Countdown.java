package com.florianraith.murder;

import com.florianraith.murder.config.Messages;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;

@RequiredArgsConstructor
public class Countdown {

    private static final Title.Times STANDALONE_TIMES = Title.Times.times(Ticks.duration(10L), Duration.ofSeconds(1), Ticks.duration(20L));
    private static final Title.Times CONTINUOUS_TIMES_FADE_IN = Title.Times.times(Ticks.duration(10L), Duration.ofSeconds(1), Duration.ZERO);
    private static final Title.Times CONTINUOUS_TIMES = Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ZERO);
    private static final Title.Times CONTINUOUS_TIMES_FADE_OUT = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Ticks.duration(20L));

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

                Component component = messages.get(
                        message,
                        Placeholder.unparsed("time", String.valueOf(countdown)),
                        Placeholder.unparsed("verb", verb)
                );

                Title title = Title.title(Component.empty(), component, getTimes());

                Bukkit.getServer().showTitle(title);
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1));
            }

            countdown--;
        }, 0, 20);
    }

    private Title.Times getTimes() {
        if (countdown > 5) {
            return STANDALONE_TIMES;
        } else if (countdown == 5) {
            return CONTINUOUS_TIMES_FADE_IN;
        } else if (countdown > 1) {
            return CONTINUOUS_TIMES;
        }

        return CONTINUOUS_TIMES_FADE_OUT;
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
