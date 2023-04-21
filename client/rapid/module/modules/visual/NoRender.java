package client.rapid.module.modules.visual;

import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "No Render", getCategory = Category.VISUAL)
public class NoRender extends Module {
    private Setting scoreboard = new Setting("Scoreboard", this, false),
    hurtcam = new Setting("Hurt Cam", this, false),
    pumpkinOverlay = new Setting("Pumpkin Overlay", this, false),
    nausea = new Setting("Nausea", this, false),
    portal = new Setting("Portal", this, false);

    public NoRender() {
        add(scoreboard, hurtcam, pumpkinOverlay, nausea, portal);
    }
}
