package client.rapid.module.modules.hud;

import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.visual.ColorUtil;

import java.awt.*;

@ModuleInfo(getName = "Hud Settings", getCategory = Category.HUD)
public class HudSettings extends Draggable {
    private final Setting colorMode = new Setting("Color", this, "Custom", "Rainbow", "Fade", "Gradient");
    private final Setting red = new Setting("Red", this, 255, 0, 255, true);
    private final Setting green = new Setting("Green", this, 100, 0, 255, true);
    private final Setting blue = new Setting("Blue", this, 100, 0, 255, true);

    private final Setting red1 = new Setting("Second Red", this, 255, 0, 255, true);
    private final Setting green1 = new Setting("Second Green", this, 100, 0, 255, true);
    private final Setting blue1 = new Setting("Second Blue", this, 100, 0, 255, true);
    private final Setting rainbow = new Setting("Rainbow Wave", this, 10, 1, 100, true);

    private final Setting mcFont = new Setting("Minecraft Font", this, true);
    private final Setting fontMode = new Setting("Font", this, "Product Sans", "Inter", "SF UI");
    private final Setting shadow = new Setting("Shadow", this, true);

    public HudSettings() {
        super(0, 0, 0, 0);
        add(colorMode, red, green, blue, red1, green1, blue1, rainbow, mcFont, fontMode, shadow);
    }

    public int getColor(long wave) {
        Color
        custom = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()),
        custom1 = new Color((int) red1.getValue(), (int) green1.getValue(), (int) blue1.getValue());
        double offset = (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (wave * 49.8) / rainbow.getValue() / 50;

        switch (colorMode.getMode()) {
            case "Custom":
                return custom.getRGB();
            case "Rainbow":
                return ColorUtil.getRainbow(4, wave * (long)rainbow.getValue());
            case "Fade":
                return ColorUtil.getGradient(custom, custom.darker().darker(), offset);
            case "Gradient":
                return ColorUtil.getGradient(custom, custom1, offset);
        }
        return -1;
    }

}
