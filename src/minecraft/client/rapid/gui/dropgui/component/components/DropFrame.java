package client.rapid.gui.dropgui.component.components;

import java.util.ArrayList;

import client.rapid.Wrapper;
import client.rapid.gui.dropgui.DropdownGui;
import client.rapid.gui.dropgui.component.DropComponent;
import client.rapid.module.Module;
import client.rapid.module.modules.Category;
import client.rapid.util.font.*;
import net.minecraft.client.gui.*;

public class DropFrame {
	public MCFontRenderer
	font = Fonts.clickgui,
	icons = Fonts.newIcons;

	public ArrayList<DropComponent> components;
	public Category category;
	private boolean open, dragging;
	private final int width;
	public int x, y, dragX, dragY;
	private final int barHeight;
	
	public DropFrame(Category cat) {
		this.components = new ArrayList<>();
		this.category = cat;
		this.width = 105;
		this.x = 5;
		this.y = 5;
		this.barHeight = 16;
		this.dragX = 0;
		this.open = true;
		this.dragging = false;
		int tY = this.barHeight;
		
		for(Module mod : Wrapper.getModuleManager().getModulesInCategory(category)) {
			DropButton modButton = new DropButton(mod, this, tY);
			this.components.add(modButton);
			tY += 12;
		}
	}
	
	public void renderFrame() {
		if(Wrapper.getSettingsManager().getSettingByName("Click Gui", "Outline").isEnabled())
			Gui.drawRect(this.x - 1.5, this.y - 0.5, this.x + this.width + 1.5, this.y + this.barHeight + 0.5, DropdownGui.hud.getColor(barHeight));

		Gui.drawRect(this.x - 1, this.y, this.x + this.width + 1, this.y + this.barHeight, DropdownGui.backgroundDark);
		font.drawString(this.category.getName(), (this.x + 2) + 5, (this.y + 2.5f) + 2.5f, -1);

		if(Wrapper.getSettingsManager().getSettingByName("Click Gui", "Category Icons").isEnabled()) {
			icons.drawString(String.valueOf(category.getIcon()), x + width - 15, y + icons.getStringHeight(String.valueOf(category.getIcon())) / 1.5f - 0.5f, -1);
			/*if(category == Category.PLAYER || category == Category.HUD)
				icon1.drawString(String.valueOf(category.getIcon()), x + width - 17, y + 4f, -1);
			else
				icon2.drawString(String.valueOf(category.getIcon()), x + width - 17, y + 4f, -1);*/
		}

		if(this.open && !this.components.isEmpty()) {
			for(DropComponent component : components)
				component.renderComponent();
		}
	}
	
	public void refresh() {
		int off = this.barHeight;
		for(DropComponent comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(this.dragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}
	
	public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}

	public ArrayList<DropComponent> getComponents() {
		return components;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void setDrag(boolean drag) {
		this.dragging = drag;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
}
