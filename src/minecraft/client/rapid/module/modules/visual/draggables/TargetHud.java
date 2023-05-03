package client.rapid.module.modules.visual.draggables;

import java.awt.Color;

import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.gui.GuiPosition;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.modules.visual.Hud;
import client.rapid.module.settings.Setting;
import client.rapid.util.font.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(getName = "Target HUD", getCategory = Category.VISUAL)
public class TargetHud extends Draggable {
	private final Setting mode = new Setting("Mode", this, "Old", "New");
	MCFontRenderer font = Fonts.normal2;

	public TargetHud() {
		super(5, 20, 130, 50);
		add(mode);
	}

	public void drawDummy(int mouseX, int mouseY) {
		Gui.drawRect(x, y, x + width, y + height, 0x90000000);
		mc.fontRendererObj.drawString(name, x + width / 2 - mc.fontRendererObj.getStringWidth(name) / 2, y + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2, -1);
		super.drawDummy(mouseX, mouseY);
	}

	public void onEvent(Event e) {
		if(e instanceof EventRender && e.isPre()) {
			if(!(mc.currentScreen instanceof GuiPosition)) {
				EntityLivingBase target = KillAura.target;

				if (!(target instanceof EntityPlayer || target instanceof EntityVillager || target instanceof EntityZombie))
					return;

				Hud hud = (Hud) Wrapper.getModuleManager().getModule("HUD");

				switch (mode.getMode()) {
					case "Old":
						Gui.drawRect(x - 1, y - 1, x + ((int) target.getMaxHealth() * 6) + 11, y + height + 1, hud.getColor(0));
						Gui.drawRect(x, y, x + (int) target.getMaxHealth() * 6 + 10, y + height, new Color(0xFF0F0F13).brighter().getRGB());
						font.drawString(target.getName(), x + 22, y + 4, -1);
						Gui.drawRect(x + 20, y + 16, x + 5 + (int) target.getMaxHealth() * 6, y + 30, 0xFF0D0E11);
						Gui.drawRect(x + 20, y + 16, x + 5 + (int) target.getHealth() * 6, y + 30, 0xFF48FF48);

						font.drawStringWithShadow("" + (int) target.getHealth() + " HP", x + 26, y + 19.5f, -1);
						GuiInventory.drawEntityOnScreen(x + 10, y + 43, 18, -20, -20, target);
						break;
					case "New":
						Gui.drawRect(x, y - 1, x + ((int) target.getMaxHealth() * 6) + 10, y, hud.getColor(0));
						Gui.drawRect(x, y, x + (int) target.getMaxHealth() * 6 + 10, y + height, 0x90000000);

						int color = 0xFF20FF20;

						if (target.getHealth() >= 13)
							color = 0xFF20FF20;
						else if (target.getHealth() < 13 && target.getHealth() > 7)
							color = 0xFFFFFF20;
						else if (target.getHealth() < 8)
							color = 0xFFFF2020;

						Gui.drawRect(x + 30, y + 16, x + 30 + (double) target.getHealth() * 4.7f, y + 30, color);

						if (Wrapper.getSettingsManager().getSettingByName("HUD", "Minecraft Font").isEnabled()) {
							mc.fontRendererObj.drawStringWithShadow(target.getName(), x + 30, y + 4, -1);
							mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", target.getHealth() / 2), x + 30 + (target.getMaxHealth() * 4.7f) / 2 - mc.fontRendererObj.getStringWidth(String.format("%.1f", target.getHealth() / 2)) / 2, y + 19f, -1);

						} else {
							font.drawString(target.getName(), x + 30, y + 4, -1);
							font.drawStringWithShadow(String.format("%.1f", target.getHealth() / 2), x + 30 + ((double) target.getMaxHealth() * 4.7f) / 2 - font.getStringWidth(String.format("%.1f", target.getHealth() / 2)) / 2, y + 20f, -1);
						}
						GuiInventory.drawEntityOnScreen(x + 14, y + 46, 22, target.rotationYaw, target.rotationPitch, target);
						break;
				}
			}
		}
	}
}
