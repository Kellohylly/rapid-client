package client.rapid.module.modules.hud;

import client.rapid.Client;
import client.rapid.event.Event;
import client.rapid.event.events.game.EventRender2D;
import client.rapid.module.Draggable;
import client.rapid.module.ModuleInfo;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.font.Fonts;
import client.rapid.util.font.MCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@ModuleInfo(getName = "Watermark", getCategory = Category.HUD)
public class Watermark extends Draggable {
	private final Setting watermarkMode = new Setting("Watermark", this, "Simple", "Cool", "Sense");
	private final Setting watermarkOpacity = new Setting("Opacity", this, 100, 0, 255, true);

	private final Setting mcFont = Client.getInstance().getSettingsManager().getSetting(HudSettings.class, "Minecraft Font");
	private final Setting rainbow = Client.getInstance().getSettingsManager().getSetting(HudSettings.class, "Rainbow Wave");

	public static String text = Client.getInstance().getName();
	MCFontRenderer font = Fonts.normal2;

	public Watermark() {
		super(3, 3, 100, 20);
		add(watermarkMode, watermarkOpacity);
	}

	@Override
	public void updateSettings() {
		watermarkOpacity.setVisible(watermarkMode.getMode().equals("Cool"));
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventRender2D && e.isPre()) {
			String text1 = text
					.replace("{fps}", String.valueOf(Minecraft.getDebugFPS()))
					.replace("{time}", new SimpleDateFormat("kk:mm").format(new Date()))
					.replace("{mcversion}", Minecraft.getMinecraft().getVersion());
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
					Gui.drawRect(2 + x, 2 + y, 3 + mc.fontRendererObj.getStringWidth(text) + x + 2, 15 + y, new Color(0, 0, 0, (int)watermarkOpacity.getValue()).getRGB());
					Gui.drawRect(2 + x, 2 + y, 3 + mc.fontRendererObj.getStringWidth(text) + x + 2, 3 + y, getColor((long)rainbow.getValue()));
					mc.fontRendererObj.drawStringWithShadow(text, 5 + x, 5 + y, getColor((long)rainbow.getValue()));
				} else {
					Gui.drawRect(2 + x, 2 + y, font.getStringWidth(text) + 8 + x, 15 + y, new Color(0, 0, 0, (int)watermarkOpacity.getValue()).getRGB());
					Gui.drawRect(2 + x, 2 + y, font.getStringWidth(text) + 8 + x, 3 + y, getColor((long)rainbow.getValue()));
					font.drawStringWithShadow(text, 5 + x, 6 + y, getColor((long)rainbow.getValue()));
				}
				break;
			case "Sense":
				Gui.drawRect(3.5 + x, 3.5 + y, 3.5 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 6 : font.getStringWidth(text) + 7) + x, 20.5 + y, 0xFF3D3F45);
				Gui.drawRect(5 + x, 5 + y, 4 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 4 : font.getStringWidth(text) + 5) + x, 19 + y, 0xFF1F201C);
				Gui.drawRect(5 + x, 4.5 + y, 4 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 4 : font.getStringWidth(text) + 5) + x, 5.5 + y, getColor((int)rainbow.getValue()));
				if(mcFont.isEnabled())
					mc.fontRendererObj.drawStringWithShadow(text, 7.5f + x, 8.5f + y, getColor((int)rainbow.getValue()));
				else
					font.drawStringWithShadow(text, 7f + x, 8.5f + y, getColor((int)rainbow.getValue()));
				break;
		}
	}

	public static void setWatermark(String text) {
		Watermark.text = text;
		savePositions();
	}

	public int getColor(long index) {
		return ((HudSettings) Client.getInstance().getModuleManager().getModule(HudSettings.class)).getColor(index);
	}

}
