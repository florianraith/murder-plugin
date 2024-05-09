package com.florianraith.murder;

import com.florianraith.murder.phase.WorldPhase;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.function.Supplier;

public class CountdownFactory {

    @Inject private MurderPlugin plugin;
    @Inject private Injector injector;

    /**
     * Creates a new countdown that executes the given runnable after the specified amount of seconds.
     */
    public Countdown create(Runnable runnable, long seconds) {
        Countdown countdown = new Countdown(runnable, seconds);
        injector.injectMembers(countdown);
        return countdown;
    }

    /**
     * Creates a new countdown that switches the current phase to the given phase after the specified amount of seconds.
     */
    public Countdown phase(Supplier<WorldPhase> phaseSupplier, long seconds) {
        return create(() -> plugin.setPhase(phaseSupplier.get()), seconds);
    }

}
