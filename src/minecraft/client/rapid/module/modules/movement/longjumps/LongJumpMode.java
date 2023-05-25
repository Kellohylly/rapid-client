package client.rapid.module.modules.movement.longjumps;

import client.rapid.module.modules.movement.longjumps.ncp.NCPDevLongJump;
import client.rapid.module.modules.movement.longjumps.ncp.OldNCPLongJump;

public enum LongJumpMode {
    VANILLA("Vanilla", new VanillaLongJump(), "Vanilla"),
    OLD_NCP("Old NCP", new OldNCPLongJump(), "NCP"),
    NCP_DEV("NCP Dev", new NCPDevLongJump(), "NCP"),
    VULCAN("Vulcan", new VulcanLongJump(), "Vulcan");

    private final String name;
    private final LongJumpBase base;
    private final String mode;

    LongJumpMode(String name, LongJumpBase base, String mode) {
        this.name = name;
        this.base = base;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public LongJumpBase getBase() {
        return base;
    }

    public String getMode() {
        return mode;
    }

}
