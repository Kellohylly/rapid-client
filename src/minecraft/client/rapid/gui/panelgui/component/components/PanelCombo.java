package client.rapid.gui.panelgui.component.components;

import client.rapid.gui.panelgui.PanelGui;
import client.rapid.gui.panelgui.component.Comp;
import client.rapid.module.Module;
import client.rapid.module.settings.Setting;

public class PanelCombo extends Comp {
    private int modeIndex;

    public PanelCombo(double x, double y, PanelGui parent, Module module, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
        this.modeIndex = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        parent.font.drawString(setting.getName(), (int)(parent.posX + x) - 18, (int)(parent.posY + y + 6 + scrollY), -1);
        parent.font.drawString(setting.getMode(), (int)(parent.posX + x + 120) - parent.font.getStringWidth(setting.getMode()), (int)(parent.posY + y + 6) + (float)scrollY, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int maxIndex = setting.getOptions().size();

        if(isInside(mouseX, mouseY, (int)(parent.posX + x - 20), (int)(parent.posY + y + 1) + scrollY, (int)(parent.posX + x + 122), (int)(parent.posY + y + 20) + scrollY)) {
            if (mouseButton == 0) {
                if (modeIndex + 1 >= maxIndex)
                    modeIndex = 0;
                else
                    modeIndex++;
            } else if (mouseButton == 1) {
                if (modeIndex <= 0)
                    modeIndex = maxIndex - 1;
                else
                    modeIndex--;
            }
            setting.setMode(setting.getOptions().get(modeIndex));
        }
    }
}
