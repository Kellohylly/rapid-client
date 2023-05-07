package client.rapid.gui.csgogui.component.components;

import client.rapid.Wrapper;
import client.rapid.gui.csgogui.CsgoGui;
import client.rapid.gui.csgogui.component.Comp;
import client.rapid.module.Module;
import client.rapid.module.modules.hud.HudSettings;
import client.rapid.module.modules.visual.Watermark;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.Gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CsgoSlider extends Comp {
    private boolean dragging = false;

    private double renderWidth;

    public CsgoSlider(double x, double y, CsgoGui parent, Module module, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        Gui.drawRect((int)(parent.posX + x - 20), (int)(parent.posY + y + 19) + scrollY, (int)(parent.posX + x + 122), (int)(parent.posY + y + 20) + scrollY, CsgoGui.backgroundDark);
        Gui.drawRect((int)(parent.posX + x - 20), (int)(parent.posY + y + 19) + scrollY, (int)(parent.posX + x - 20) + renderWidth, (int)(parent.posY + y + 20) + scrollY, ((HudSettings) Wrapper.getModuleManager().getModule("Hud Settings")).getColor((long)y));
        Gui.drawRect((int)(parent.posX + x - 17) + renderWidth, (int)(parent.posY + y + 17) + scrollY, (int)(parent.posX + x - 22) + renderWidth, (int)(parent.posY + y + 22) + scrollY, ((HudSettings) Wrapper.getModuleManager().getModule("Hud Settings")).getColor((long)y));
        parent.font.drawString(setting.getName(), (int)(parent.posX + x) - 18, (int)(parent.posY + y + 6) + (float)scrollY, -1);
        parent.font.drawString(String.valueOf(setting.getValue()), (int)(parent.posX + x + 120) - parent.font.getStringWidth(String.valueOf(setting.getValue())), (int)(parent.posY + y + 6) + (float)scrollY, -1);

        double
        diff = Math.min(142, Math.max(0, mouseX - (parent.posX + x) + 20)),
        min = setting.getMin(),
        max = setting.getMax();

        renderWidth = (142) * (setting.getValue() - min) / (max - min);

        if (dragging) {
            if (diff == 0)
                setting.setValue(setting.getMin());

            else {
                double newValue = roundToPlace(((diff / 142) * (max - min) + min));
                setting.setValue(newValue);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(isInside(mouseX, mouseY, (int)(parent.posX + x - 20), (int)(parent.posY + y + 1) + scrollY, (int)(parent.posX + x + 122), (int)(parent.posY + y + 20) + scrollY) && mouseButton == 0)
            dragging = true;

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    private static double roundToPlace(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
