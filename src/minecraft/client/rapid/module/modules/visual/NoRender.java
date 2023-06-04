package client.rapid.module.modules.visual;

import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;

@ModuleInfo(getName = "No Render", getCategory = Category.VISUAL)
public class NoRender extends Module {
    private final Setting hurtcam = new Setting("Hurt Cam", this, false);
    private final Setting pumpkinOverlay = new Setting("Pumpkin Overlay", this, false);
    private final Setting nausea = new Setting("Nausea", this, false);
    private final Setting portal = new Setting("Portal", this, false);

    public NoRender() {
        add(hurtcam, pumpkinOverlay, nausea, portal);
    }

}
