package client.rapid.module.modules.movement.jesus;

import client.rapid.module.modules.movement.jesus.jump.VerusJesus;
import client.rapid.module.modules.movement.jesus.jump.VulcanJesus;
import client.rapid.module.modules.movement.jesus.other.MatrixJesus;
import client.rapid.module.modules.movement.jesus.other.SolidJesus;

public enum JesusMode {
    VULCAN_JUMP("Vulcan", new VulcanJesus(), "Jump"),
    VERUS_JUMP("Verus", new VerusJesus(), "Jump"),
    MATRIX_MOTION("Matrix", new MatrixJesus(), "Matrix"),
    SOLID("Solid", new SolidJesus(), "Solid");

    private final String name;
    private final JesusBase base;
    private final String mode;

    JesusMode(String name, JesusBase base, String mode) {
        this.name = name;
        this.base = base;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public JesusBase getBase() {
        return base;
    }

    public String getMode() {
        return mode;
    }
}
