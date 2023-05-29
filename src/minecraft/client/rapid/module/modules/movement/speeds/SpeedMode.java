package client.rapid.module.modules.movement.speeds;

import client.rapid.module.modules.movement.speeds.ncp.NCPHopSpeed;
import client.rapid.module.modules.movement.speeds.ncp.NCPLowHopSpeed;
import client.rapid.module.modules.movement.speeds.ncp.NCPYPortSpeed;
import client.rapid.module.modules.movement.speeds.vanilla.StrafeSpeed;
import client.rapid.module.modules.movement.speeds.vanilla.VanillaSpeed;
import client.rapid.module.modules.movement.speeds.verus.VerusGroundSpeed;
import client.rapid.module.modules.movement.speeds.verus.VerusHopSpeed;
import client.rapid.module.modules.movement.speeds.vulcan.VulcanGroundSpeed;
import client.rapid.module.modules.movement.speeds.vulcan.VulcanHopSpeed;
import client.rapid.module.modules.movement.speeds.vulcan.VulcanLowHopSpeed;

public enum SpeedMode {
    MOTION("Motion", new VanillaSpeed(), "Vanilla"),
    STRAFE("Strafe", new StrafeSpeed(), "Vanilla"),
    NCP_HOP("NCP Hop", new NCPHopSpeed(), "NCP"),
    NCP_LOWHOP("NCP LowHop", new NCPLowHopSpeed(), "NCP"),
    NCP_YPORT("NCP YPort", new NCPYPortSpeed(), "NCP"),
    VULCAN_HOP("Vulcan Hop", new VulcanHopSpeed(), "Vulcan"),
    VULCAN_LOWHOP("Vulcan LowHop", new VulcanLowHopSpeed(), "Vulcan"),
    VULCAN_GROUND("Vulcan Ground", new VulcanGroundSpeed(), "Vulcan"),
    VERUS_HOP("Verus Hop", new VerusHopSpeed(), "Verus"),
    VERUS_GROUND("Verus Ground", new VerusGroundSpeed(), "Verus");

    private final String name;
    private final SpeedBase base;
    private final String mode;

    SpeedMode(String name, SpeedBase base, String mode) {
        this.name = name;
        this.base = base;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public SpeedBase getBase() {
        return base;
    }

    public String getMode() {
        return mode;
    }

}
