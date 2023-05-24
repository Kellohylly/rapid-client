package client.rapid.module.modules.movement.flights;

import client.rapid.module.modules.movement.flights.vanilla.CreativeFlight;
import client.rapid.module.modules.movement.flights.vanilla.MotionFlight;
import client.rapid.module.modules.movement.flights.verus.CollideFlight;
import client.rapid.module.modules.movement.flights.verus.VerusFastFlight;

public enum FlightMode {
    MOTION("Motion", new MotionFlight(), "Vanilla"),
    CREATIVE("Creative", new CreativeFlight(), "Vanilla"),
    OLDNCP("Old NCP", new OldNCPFlight(), "Old NCP"),
    COLLIDE("Collide", new CollideFlight(), "Verus"),
    VERUSFAST("Verus Fast", new VerusFastFlight(), "Verus");

    private final String name;
    private final FlightBase base;
    private final String mode;

    FlightMode(String name, FlightBase base, String mode) {
        this.name = name;
        this.base = base;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public FlightBase getBase() {
        return base;
    }

    public String getMode() {
        return mode;
    }

}
