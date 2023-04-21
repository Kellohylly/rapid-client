package client.rapid.gui.clickgui.component.components;

import java.awt.Color;
import java.math.*;

import client.rapid.gui.clickgui.ClickGui;
import client.rapid.gui.clickgui.component.Button;
import client.rapid.gui.clickgui.component.Component;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.Gui;

public class Slider extends Component {
	private final Setting set;
	private int offset;
	private int x, y;
	private boolean hovered, dragging = false;

	private double renderWidth;

	public Slider(Setting value, Button button, int offset) {
		this.set = value;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + offset + 12, this.hovered ? new Color(0xFF0F0F13).darker().getRGB() : 0xFF0F0F13);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth + 2, parent.parent.getY() + offset + 12,hovered ? new Color(ClickGui.hud.getColor(offset * 9L)).darker().darker().getRGB() : new Color(ClickGui.hud.getColor(offset * 9L)).darker().getRGB());
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + 12,hovered ? new Color(ClickGui.hud.getColor(offset * 9L)).darker().getRGB() : ClickGui.hud.getColor(offset * 9L));
		parent.parent.font.drawString(this.set.getName(), (parent.parent.getX() + 4), (parent.parent.getY() + offset + 2) + 1.5f, -1);
		parent.parent.font.drawString("" + this.set.getValue(), (parent.parent.getX() + 100) - parent.parent.font.getStringWidth("" + set.getValue()) + 3, (parent.parent.getY() + offset + 2) + 1.5f, -1);
	}
	
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();

		double
		diff = Math.min(103, Math.max(0, mouseX - this.x)),
		min = set.getMin(),
		max = set.getMax();
		
		renderWidth = (103) * (set.getValue() - min) / (max - min);
		
		if (dragging) {
			if (diff == 0)
				set.setValue(set.getMin());

			else {
				double newValue = roundToPlace(((diff / 103) * (max - min) + min));
				set.setValue(newValue);
			}
		}
	}

	private static double roundToPlace(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open)
			dragging = true;

		if(isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open)
			dragging = true;
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public boolean isMouseOnButtonD(int x, int y) {
		return x > this.x && x < this.x + (parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12;
	}
	
	public boolean isMouseOnButtonI(int x, int y) {
		return x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + 12;
	}

	public void setOff(int newOff) {
		offset = newOff;
	}

}
