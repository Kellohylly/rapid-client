package client.rapid.gui.dropgui.component.components;

import java.awt.Color;

import client.rapid.gui.dropgui.component.DropComponent;
import client.rapid.gui.panelgui.PanelGui;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.Gui;

public class DropCombo extends DropComponent {
	private int modeIndex;
	private int offset;
	private int x;
	private int y;

	public DropCombo(Setting set, DropButton button, int offset) {
		this.set = set;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
		this.modeIndex = 0;
	}

	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()) + 1, parent.parent.getY() + offset + 12, this.hovered ? new Color(PanelGui.backgroundDark).darker().getRGB() : PanelGui.backgroundDark);
		parent.parent.font.drawString(set.getName(), (parent.parent.getX() + 4), (parent.parent.getY() + offset + 2) + 1.5f, -1);
		parent.parent.font.drawString(set.getMode(), (parent.parent.getX() + 100) - parent.parent.font.getStringWidth(set.getMode()) + 3, (parent.parent.getY() + offset + 2) + 1.5f, -1);
	}
	
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(x, y, mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(x, y, mouseX, mouseY) && this.parent.open) {
			int maxIndex = set.getOptions().size();

			if(button == 0) {
				if(modeIndex + 1 >= maxIndex)
					modeIndex = 0;
				else
					modeIndex++;
			} else if(button == 1) {
				if(modeIndex <= 0)
					modeIndex = maxIndex - 1;
				else
					modeIndex--;
			}
			set.setMode(set.getOptions().get(modeIndex));
		}
	}

	public void setOff(int newOff) {
		offset = newOff;
	}

}
