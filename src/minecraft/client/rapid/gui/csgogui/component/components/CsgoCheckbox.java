package client.rapid.gui.csgogui.component.components;

import client.rapid.Wrapper;
import client.rapid.gui.csgogui.CsgoGui;
import client.rapid.gui.csgogui.component.Comp;
import client.rapid.module.Module;
import client.rapid.module.modules.visual.Hud;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.Gui;

public class CsgoCheckbox extends Comp {

    public CsgoCheckbox(double x, double y, CsgoGui parent, Module module, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        parent.font.drawString(setting.getName(), (int)(parent.posX + x) - 18, (int)(parent.posY + y + 6) + (float)scrollY, -1);

        Gui.drawRect((int)(parent.posX + x + 107), (int)(parent.posY + y + 4) + scrollY, (int)(parent.posX + x + 120), (int)(parent.posY + y + 17) + scrollY, CsgoGui.backgroundDark);

        if(setting.isEnabled())
            Gui.drawRect((int)(parent.posX + x + 109), (int)(parent.posY + y + 6) + scrollY, (int)(parent.posX + x + 118), (int)(parent.posY + y + 15) + scrollY, ((Hud) Wrapper.getModuleManager().getModule("HUD")).getColor((long)y));

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(isInside(mouseX, mouseY, (int)(parent.posX + x - 20), (int)(parent.posY + y + 1) + scrollY, (int)(parent.posX + x + 122), (int)(parent.posY + y + 20) + scrollY) && mouseButton == 0)
            setting.setEnabled(!setting.isEnabled());
    }
}
