package client.rapid.gui.clickgui.component.components;

import java.awt.Color;

import client.rapid.gui.clickgui.ClickGui;
import client.rapid.gui.clickgui.component.*;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.Gui;

public class Checkbox extends Component {
	private int offset, x, y;

	public Checkbox(Setting option, Button button, int offset) {
		this.set = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()) + 1, parent.parent.getY() + offset + 12, this.hovered ? new Color(0xFF0F0F13).darker().getRGB() : 0xFF0F0F13);
		parent.parent.font.drawString(this.set.getName(), (parent.parent.getX() + 2) + 2, (parent.parent.getY() + offset + 2) + 1.5f, -1);
		Gui.drawRect(parent.parent.getX() + 95, parent.parent.getY() + offset + 2, parent.parent.getX() + 103, parent.parent.getY() + offset + 10, new Color(0xFF0F0F13).brighter().brighter().getRGB());

		if(set.isEnabled())
			Gui.drawRect(parent.parent.getX() + 96, parent.parent.getY() + offset + 3, parent.parent.getX() + 102, parent.parent.getY() + offset + 9, ClickGui.hud.getColor(offset * 9L));
	}

	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(x, y, mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(x, y, mouseX, mouseY) && button == 0 && this.parent.open)
			set.setEnabled(!set.isEnabled());
	}

	public void setOff(int newOff) {
		offset = newOff;
	}
}
