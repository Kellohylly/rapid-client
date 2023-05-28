package client.rapid.gui.dropgui.component.components;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import client.rapid.gui.dropgui.DropdownGui;
import client.rapid.gui.dropgui.component.DropComponent;
import client.rapid.gui.panelgui.PanelGui;
import client.rapid.module.settings.Setting;
import net.minecraft.client.gui.Gui;

public class DropSlider extends DropComponent {
	private int offset;
	private int x;
	private int y;
	private boolean hovered;
	private boolean dragging = false;

	private double renderWidth;

	public DropSlider(Setting value, DropButton button, int offset) {
		this.set = value;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() - 1, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 1, parent.parent.getY() + offset + 12, this.hovered ? new Color(PanelGui.backgroundDark).darker().getRGB() : PanelGui.backgroundDark);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth + 2, parent.parent.getY() + offset + 12,hovered ? new Color(DropdownGui.hud.getColor(offset / 2)).darker().darker().getRGB() : new Color(DropdownGui.hud.getColor(offset / 2)).darker().getRGB());
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + 12,hovered ? new Color(DropdownGui.hud.getColor(offset / 2)).darker().getRGB() : DropdownGui.hud.getColor(offset / 2));
		parent.parent.font.drawString(this.set.getName(), (parent.parent.getX() + 4), (parent.parent.getY() + offset + 2) + 1.5f, -1);
		parent.parent.font.drawString(String.valueOf(this.set.getValue()), (parent.parent.getX() + 100) - parent.parent.font.getStringWidth(String.valueOf(set.getValue())) + 3, (parent.parent.getY() + offset + 2) + 1.5f, -1);
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
			if (diff == 0) {
				set.setValue(set.getMin());

			} else {
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
		if(isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}

		if(isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public boolean isMouseOnButtonD(int x, int y) {
		return x > this.x && x < this.x + (parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 13;
	}
	
	public boolean isMouseOnButtonI(int x, int y) {
		return x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + 13;
	}

	public void setOff(int newOff) {
		offset = newOff;
	}

}
