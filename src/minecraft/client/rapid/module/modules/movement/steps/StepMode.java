package client.rapid.module.modules.movement.steps;

import client.rapid.module.modules.movement.steps.motion.KarhuStep;
import client.rapid.module.modules.movement.steps.motion.MatrixStep;
import client.rapid.module.modules.movement.steps.motion.NCPStep;
import client.rapid.module.modules.movement.steps.packet.OldNCPStep;

public enum StepMode {
    KARHU("Karhu", new KarhuStep(), "Motion"),
    MATRIX("Matrix", new MatrixStep(), "Motion"),
    NCP("NCP", new NCPStep(), "Motion"),
    OLD_NCP("Old NCP", new OldNCPStep(), "Old NCP"),
    VANILLA("Vanilla", new VanillaStep(), "Vanilla");

    private final String name;
    private final StepBase base;
    private final String mode;

    StepMode(String name, StepBase base, String mode) {
        this.name = name;
        this.base = base;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public StepBase getBase() {
        return base;
    }

    public String getMode() {
        return mode;
    }
}
