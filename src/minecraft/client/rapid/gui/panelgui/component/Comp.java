package client.rapid.gui.panelgui.component;

import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventSettingChange;
import client.rapid.gui.panelgui.PanelGui;
import client.rapid.module.Module;
import client.rapid.module.settings.Setting;

public class Comp {
    public double x, y, scrollY;
    public PanelGui parent;
    public Module module;
    public Setting setting;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
    public void mouseReleased(int mouseX, int mouseY, int state) {}
    public void drawScreen(int mouseX, int mouseY) {}
    public void keyTyped(char typedChar, int keyCode) {}

    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    public void setY(double y) {
        this.y = y;
    }
}
