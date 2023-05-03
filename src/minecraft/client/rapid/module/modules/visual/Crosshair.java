package client.rapid.module.modules.visual;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.Gui;

@ModuleInfo(getName = "Crosshair", getCategory = Category.VISUAL)
public class Crosshair extends Module {
    private final Setting length = new Setting("Length", this, 1, 1, 10, false);
    private final Setting space = new Setting("Space", this, 1, 1, 10, false);
    private final Setting thickness = new Setting("Thickness", this, 1, 1, 10, false);
    private final Setting borderThickness = new Setting("Border Thickness", this, 1, 1, 10, false);
    private final Setting dot = new Setting("Dot", this, true);

    public Crosshair() {
        add(length, space, thickness, borderThickness, dot);
    }

    public void onEvent(Event e) {
        if(e instanceof EventRender && e.isPre()) {
            EventRender event = (EventRender)e;

            double width = (float) event.getScaledResolution().getScaledWidth() / 2 + 0.5;
            double height = (float) event.getScaledResolution().getScaledHeight() / 2 + 0.5;
            double space = this.space.getValue() - 3;
            double thickness = this.thickness.getValue() / 5;
            double border = this.borderThickness.getValue() / 5;
            double length = this.length.getValue() - 4;

            // Horizontal Border
            Gui.drawRect(width + 3 + space - border, height + 0.5 + thickness + border, width + 10 + space + border + length, height - 0.5 - thickness - border, 0xFF000000);
            Gui.drawRect(width - 3 - space + border, height + 0.5 + thickness + border, width - 10 - space - border - length, height - 0.5 - thickness - border, 0xFF000000);

            // Vertical Border
            Gui.drawRect(width + 0.5 + thickness + border, height - 3 - space + border, width - 0.5 - thickness - border, height - 10 - space - border - length, 0xFF000000);
            Gui.drawRect(width + 0.5 + thickness + border, height + 3 + space - border, width - 0.5 - thickness - border, height + 10 + space + border + length, 0xFF000000);

            if(dot.isEnabled()) {
                Gui.drawRect(width - 1 - thickness - border, height - 1 - thickness - border, width + 1 + thickness + border, height + 1 + thickness + border, 0xFF000000);
                Gui.drawRect(width - 1 - thickness, height - 1 - thickness, width + 1 + thickness, height + 1 + thickness, -1);
            }
            // Horizontal bars
            Gui.drawRect(width + 3 + space, height + 0.5 + thickness, width + 10 + space + length, height - 0.5 - thickness, -1);
            Gui.drawRect(width - 3 - space, height + 0.5 + thickness, width - 10 - space - length, height - 0.5 - thickness, -1);

            // Vertical
            Gui.drawRect(width + 0.5 + thickness, height - 3 - space, width - 0.5 - thickness, height - 10 - space - length, -1);
            Gui.drawRect(width + 0.5 + thickness, height + 3 + space, width - 0.5 - thickness, height + 10 + space + length, -1);
        }
    }
}
