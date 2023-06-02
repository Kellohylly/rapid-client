package client.rapid.module.modules.movement.noslows;

import client.rapid.module.modules.movement.noslows.ncp.NCPNoSlow;
import client.rapid.module.modules.movement.noslows.ncp.OldNCPNoSlow;
import client.rapid.module.modules.movement.noslows.other.VanillaNoSlow;

public enum NoSlowMode {
    VANILLA("Vanilla", new VanillaNoSlow(), "Vanilla"),
    OLDNCP("Old NCP", new OldNCPNoSlow(), "NCP"),
    NCP("Updated NCP", new NCPNoSlow(), "NCP");

    private final String name;
    private final NoSlowBase base;
    private final String mode;

    NoSlowMode(String name, NoSlowBase base, String mode) {
        this.name = name;
        this.base = base;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public NoSlowBase getBase() {
        return base;
    }

    public String getMode() {
        return mode;
    }

}
