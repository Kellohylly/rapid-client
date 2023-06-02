package client.rapid.gui.dropgui.component.components;

import java.awt.Color;
import java.util.ArrayList;

import client.rapid.Client;
import client.rapid.gui.dropgui.DropFrame;
import client.rapid.gui.dropgui.DropdownGui;
import client.rapid.gui.dropgui.component.DropComponent;
import client.rapid.gui.panelgui.PanelGui;
import client.rapid.module.modules.visual.ClickGuiToggle;
import net.minecraft.util.ResourceLocation;

import client.rapid.module.Module;
import client.rapid.module.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class DropButton extends DropComponent {
	private final ArrayList<DropComponent> subcomponents;

	public Module mod;
	public DropFrame parent;
	private boolean hovered;
	public boolean open;
	public int offset;

	public DropButton(Module mod, DropFrame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<>();
		this.open = false;
		int opY = offset + 12;

		if(Client.getInstance().getSettingsManager().getSettingsByMod(mod) != null) {
			for(Setting s : Client.getInstance().getSettingsManager().getSettingsByMod(mod)) {
				if(s.isCombo()) {
					this.subcomponents.add(new DropCombo(s, this, opY));
				}

				if(s.isSlider()) {
					this.subcomponents.add(new DropSlider(s, this, opY));
				}

				if(s.isCheck()) {
					this.subcomponents.add(new DropCheckbox(s, this, opY));
				}

				opY += 12;
			}
		}

	}
	
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 12;
		for(DropComponent comp : this.subcomponents) {
			if(comp.set.isVisible()) {
				comp.setOff(opY);
				opY += 12;
			}
		}
	}
	
	public void renderComponent() {
		if(Client.getInstance().getSettingsManager().getSetting(ClickGuiToggle.class, "Outline").isEnabled()) {
			Gui.drawRect(parent.getX() - 1.5, this.parent.getY() + this.offset, parent.getX() + parent.getWidth() + 1.5, this.parent.getY() + 13.5 + this.offset, DropdownGui.hud.getColor(offset));
		}

		Gui.drawRect(parent.getX() - 1, this.parent.getY() + this.offset, parent.getX() + parent.getWidth() + 1, this.parent.getY() + 13 + this.offset, PanelGui.backgroundDark);
		Gui.drawRect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + 12 + this.offset, this.hovered ? (mod.isEnabled() ? new Color(DropdownGui.hud.getColor(offset / 2)).darker().getRGB() : PanelGui.backgroundDark) : (mod.isEnabled() ? DropdownGui.hud.getColor(offset / 2) : new Color(PanelGui.background).getRGB()));
		parent.font.drawString(this.mod.getName(), (parent.getX() + 2) + 2, (parent.getY() + offset + 2) + 1, -1);

		if(this.subcomponents.size() > 0) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(this.open ? "rapid/images/up.png": "rapid/images/down.png"));
			Gui.drawModalRectWithCustomSizedTexture((parent.getX() + parent.getWidth() - 12), (parent.getY() + offset + 14) - 12, 0, 0, 9, 9, 9, 9);
		}

		if(this.open && !this.subcomponents.isEmpty()) {
			if(Client.getInstance().getSettingsManager().getSetting(ClickGuiToggle.class, "Outline").isEnabled()) {
				Gui.drawRect(parent.getX() - 1.5, parent.getY() + this.offset + 12, parent.getX() + 106.5, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12) + 0.5, DropdownGui.hud.getColor(offset / 2));
			}

			for(DropComponent comp : this.subcomponents) {
				comp.set.check();

				if (comp.set.isVisible()) {
					comp.renderComponent();
				}
			}
		}
	}
	
	public int getHeight() {
		int i = 0;
		for(DropComponent comp : subcomponents) {
			if(comp.set.isVisible()) {
				i++;
			}
		}
		if(this.open) {
			return (12 * (i + 1));
		}
		return 12;
	}
	
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isHovered(mouseX, mouseY);

		if(!this.subcomponents.isEmpty()) {
			for(DropComponent comp : this.subcomponents) {
				comp.set.check();

				if (comp.set.isVisible()) {
					comp.updateComponent(mouseX, mouseY);
				}
			}
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		int opY = parent.barHeight;

		if(isHovered(mouseX, mouseY) && button == 0) {
			this.mod.toggle();
		} else if(isHovered(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			subcomponents.clear();

			if(Client.getInstance().getSettingsManager().getSettingsByMod(mod) != null) {
				for(Setting s : Client.getInstance().getSettingsManager().getSettingsByMod(mod)) {
					if(s.isCombo()) {
						this.subcomponents.add(new DropCombo(s, this, opY));
					}

					if(s.isSlider()) {
						this.subcomponents.add(new DropSlider(s, this, opY));
					}

					if(s.isCheck()) {
						this.subcomponents.add(new DropCheckbox(s, this, opY));
					}

					opY += 12;
				}
			}
		}
		for(DropComponent comp : this.subcomponents) {
			comp.set.check();

			if(comp.set.isVisible()) {
				comp.mouseClicked(mouseX, mouseY, button);
			}
		}
		parent.refresh();
	}
	
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (DropComponent comp : this.subcomponents) {
			comp.set.check();

			if(comp.set.isVisible()) {
				comp.mouseReleased(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	public void keyTyped(char typedChar, int key) {
		for(DropComponent comp : this.subcomponents) {
			comp.set.check();

			if (comp.set.isVisible()) {
				comp.keyTyped(typedChar, key);
			}
		}
	}
	
	public boolean isHovered(int x, int y) {
		return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 13 + this.offset;
	}

}
