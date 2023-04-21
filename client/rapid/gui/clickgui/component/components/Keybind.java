package client.rapid.gui.clickgui.component.components;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import client.rapid.gui.clickgui.component.Component;
import client.rapid.gui.clickgui.component.Button;
import net.minecraft.client.gui.Gui;

public class Keybind extends Component {
	private boolean binding;
	private int offset, x, y;

	public Keybind(Button button, int offset) {
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;

	}

	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()) + 1, parent.parent.getY() + offset + 12, this.hovered ? new Color(0xFF0F0F13).darker().getRGB() : 0xFF0F0F13);
		parent.parent.font.drawString("Key", (parent.parent.getX() + 4), (parent.parent.getY() + offset + 2) + 1.5f, -1);
		parent.parent.font.drawString(binding ? "WAITING..." : Keyboard.getKeyName(parent.mod.getKey()), (parent.parent.getX() + 100) - parent.parent.font.getStringWidth(binding ? "Listening..." : Keyboard.getKeyName(this.parent.mod.getKey())) + 3, (parent.parent.getY() + offset + 2) + 1.5f, -1);

	}
	
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(x, y, mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(x, y, mouseX, mouseY) && button == 0 && this.parent.open)
			this.binding = !binding;
	}
	
	public void keyTyped(char typedChar, int key) {
		if(this.binding) {
			this.parent.mod.setKey(key);
			this.binding = false;
		}
	}

	public void setOff(int newOff) {
		offset = newOff;
	}

}
