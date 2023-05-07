package client.rapid.module.modules.visual;

import client.rapid.Client;
import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.module.Draggable;
import client.rapid.module.Module;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.modules.hud.HudSettings;
import client.rapid.module.settings.Setting;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import client.rapid.util.visual.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

@ModuleInfo(getName = "Watermark", getCategory = Category.HUD)
public class Watermark extends Draggable {
	private final Setting watermarkMode = new Setting("Watermark", this, "Simple", "Cool", "Sense");
	private final Setting watermarkOpacity = new Setting("Opacity", this, 100, 0, 255, true);

	private final Setting mcFont = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Minecraft Font");
	private final Setting rainbow = Wrapper.getSettingsManager().getSettingByName("Hud Settings", "Rainbow Wave");

	public static String text = Client.getInstance().getName();
	MCFontRenderer font = Fonts.normal2;

	public Watermark() {
		super(100, 100, 100, 20);
		add(watermarkMode, watermarkOpacity);
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventRender && e.isPre()) {
			String text1 = text
					.replace("{fps}", String.valueOf(Minecraft.getDebugFPS()))
					.replace("{time}", new SimpleDateFormat("kk:mm").format(new Date()))
					.replace("{mcversion}", Minecraft.getMinecraft().getVersion());

			// remove the stupid space at the end thanks to the args in the command system.
			if (text1.charAt(text1.length() - 1) == ' ')
				text1.replace(text1.substring(text1.length() - 1), "");

			drawWatermark(text1, x, y);
		}
	}

	private void drawWatermark(String text, int x, int y) {
		switch(watermarkMode.getMode()) {
			case "Simple":
				if(mcFont.isEnabled())
					mc.fontRendererObj.drawStringWithShadow(text, 2 + x, 2 + y, getColor((long)rainbow.getValue()));
				else
					font.drawStringWithShadow(text, 2 + x, 2 + y, getColor((long)rainbow.getValue()));
				break;
			case "Cool":
				if(mcFont.isEnabled()) {
					Gui.drawRect(2 + x, 2 + y, 3 + mc.fontRendererObj.getStringWidth(text) + x, 15 + y, new Color(0, 0, 0, (int)watermarkOpacity.getValue()).getRGB());
					Gui.drawRect(2 + x, 2 + y, 3 + mc.fontRendererObj.getStringWidth(text) + x, 3 + y, getColor((long)rainbow.getValue()));
					mc.fontRendererObj.drawStringWithShadow(text, 5 + x, 5 + y, -1);
				} else {
					Gui.drawRect(2 + x, 2 + y, font.getStringWidth(text) + 6 + x, 15 + y, new Color(0, 0, 0, (int)watermarkOpacity.getValue()).getRGB());
					Gui.drawRect(2 + x, 2 + y, font.getStringWidth(text) + 6 + x, 3 + y, getColor((long)rainbow.getValue()));
					font.drawStringWithShadow(text, 5 + x, 6 + y, -1);
				}
				break;
			case "Sense":
				Gui.drawRect(3.5 + x, 3.5 + y, 3.5 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 4 : font.getStringWidth(text) + 5) + x, 20.5 + y, 0xFF3D3F45);
				Gui.drawRect(5 + x, 5 + y, 4 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 2 : font.getStringWidth(text) + 3) + x, 19 + y, 0xFF1F201C);
				Gui.drawRect(5 + x, 4.5 + y, 4 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 2 : font.getStringWidth(text) + 3) + x, 5.5 + y, getColor((int)rainbow.getValue()));
				if(mcFont.isEnabled())
					mc.fontRendererObj.drawStringWithShadow(text, 7.5f + x, 8.5f + y, getColor((int)rainbow.getValue()));
				else
					font.drawStringWithShadow(text, 7f + x, 8.5f + y, getColor((int)rainbow.getValue()));
				break;
		}
	}

	public static void setWatermark(String watermark) {
		text = watermark;
		save();

	}

	public int getColor(long index) {
		return ((HudSettings)Wrapper.getModuleManager().getModule("Hud Settings")).getColor(index);
	}

}
