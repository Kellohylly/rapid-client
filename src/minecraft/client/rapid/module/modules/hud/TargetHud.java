package client.rapid.module.modules.hud;

import java.awt.Color;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.event.events.game.EventRender2D;
import client.rapid.gui.GuiPosition;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.combat.KillAura;
import client.rapid.module.settings.Setting;
import client.rapid.util.animation.Animation;
import client.rapid.util.font.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(getName = "Target HUD", getCategory = Category.HUD)
public class TargetHud extends Draggable {
	private final Setting mode = new Setting("Mode", this, "Old", "New");
	MCFontRenderer font = Fonts.normal2;

	private final Animation animation = new Animation(1, 0.1f);

	public TargetHud() {
		super(0, 0, 130, 50);
		setX(new ScaledResolution(mc).getScaledWidth() / 2 + this.getWidth());
		setY(new ScaledResolution(mc).getScaledHeight() / 2);
		add(mode);
	}

	public void onEvent(Event e) {
		if(e instanceof EventRender2D && e.isPre()) {
			EntityLivingBase target;

			if(mc.currentScreen instanceof GuiPosition) {
				target = mc.thePlayer;
			} else {
				target = KillAura.target;
			}

			if (!(target instanceof EntityPlayer || target instanceof EntityVillager || target instanceof EntityZombie))
				return;

			HudSettings hud = (HudSettings) Client.getInstance().getModuleManager().getModule(HudSettings.class);

			if(animation.getValueF() != target.getHealth()) {
				animation.interpolate(target.getHealth());
			}

			switch (mode.getMode()) {
				case "Old":
					Gui.drawRect(x - 1, y - 1, x + ((int) target.getMaxHealth() * 6) + 11, y + height + 1, hud.getColor(0));
					Gui.drawRect(x, y, x + (int) target.getMaxHealth() * 6 + 10, y + height, new Color(0xFF0F0F13).brighter().getRGB());
					font.drawString(target.getName(), x + 22, y + 4, -1);
					Gui.drawRect(x + 20, y + 16, x + 5 + target.getMaxHealth() * 6, y + 30, 0xFF0D0E11);
					Gui.drawRect(x + 20, y + 16, x + 5 + animation.getValueF() * 6, y + 30, 0xFF48FF48);

					font.drawStringWithShadow(target.getHealth() + " HP", x + 26, y + 19.5f, -1);
					GuiInventory.drawEntityOnScreen(x + 10, y + 43, 18, -20, -20, target);
					break;
				case "New":
					Gui.drawRect(x, y, x + target.getMaxHealth() * 6 + 10, y + height, 0x90000000);
					Gui.drawRect(x, y + 1, x + ((int) target.getMaxHealth() * 6) + 10, y, hud.getColor(0));

					int color = 0xFF20FF20;

					if (target.getHealth() < 13 && target.getHealth() > 7)
						color = 0xFFFFFF20;
					else if (target.getHealth() < 8)
						color = 0xFFFF2020;

					Gui.drawRect(x + 30, y + 16, x + 30 + animation.getValueF() * 4.7f, y + 30, color);

					if (Client.getInstance().getSettingsManager().getSetting(HudSettings.class, "Minecraft Font").isEnabled()) {
						mc.fontRendererObj.drawStringWithShadow(target.getName(), x + 30, y + 4, -1);
						mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", target.getHealth() / 2), x + 30 + (target.getMaxHealth() * 4.7f) / 2 - mc.fontRendererObj.getStringWidth(String.format("%.1f", target.getHealth() / 2)) / 2, y + 19f, -1);

					} else {
						font.drawString(target.getName(), x + 30, y + 4, -1);
						font.drawStringWithShadow(String.format("%.1f", target.getHealth() / 2), x + 30 + (target.getMaxHealth() * 4.7f) / 2 - font.getStringWidth(String.format("%.1f", target.getHealth() / 2)) / 2, y + 20f, -1);
					}
					GuiInventory.drawEntityOnScreen(x + 14, y + 46, 22, target.rotationYaw, target.rotationPitch, target);
					break;
			}

		}
	}

}
