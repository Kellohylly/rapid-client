package client.rapid.module.modules.visual;

import client.rapid.Client;
import client.rapid.Wrapper;
import client.rapid.event.events.Event;
import client.rapid.event.events.game.EventRender;
import client.rapid.module.*;
import client.rapid.module.modules.Category;
import client.rapid.module.settings.Setting;
import client.rapid.util.font.*;
import client.rapid.util.visual.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

@ModuleInfo(getName = "HUD", getCategory = Category.VISUAL)
public class Hud extends Draggable {
	private final Setting
			// Watermark
			watermarkMode = new Setting("Watermark", this, "Simple", "Cool", "Sense"),
			watermarkOpacity = new Setting("Opacity", this, 100, 0, 255, true),

			// Modules List
			listBarMode = new Setting("Bars", this, "None", "Front", "Back", "Back2", "Top", "Outline"),
			listOpacity = new Setting("List Opacity", this, 100, 0, 255, true),
			listHeight = new Setting("List Height", this, 6, 0, 16, true),
			lowercased = new Setting("Lowercased", this, false),
			modeTags = new Setting("Mode Tags", this, true),
			flipHorizontally = new Setting("Flip Horizontally", this, false),
			/*flipVertically = new Setting("Flip Vertically", this, false),*/
			hideVisual = new Setting("Hide Visuals", this, false),
			flipTags = new Setting("Flip Mode Tags", this, false),


	// Color
			colorMode = new Setting("Color", this, "Custom", "Rainbow", "Fade", "Gradient"),
			red = new Setting("Red", this, 255, 0, 255, true),
			green = new Setting("Green", this, 100, 0, 255, true),
			blue = new Setting("Blue", this, 100, 0, 255, true),

			// Gradient Color
			red1 = new Setting("Second Red", this, 255, 0, 255, true),
			green1 = new Setting("Second Green", this, 100, 0, 255, true),
			blue1 = new Setting("Second Blue", this, 100, 0, 255, true),
			rainbow = new Setting("Rainbow Wave", this, 10, 1, 100, true),
			mcFont = new Setting("Minecraft Font", this, true);

	public static String text = Client.getInstance().getName();
	MCFontRenderer font = Fonts.normal2;

	public Hud() {
		super(100, 100, 50, 50);
		add(watermarkMode, watermarkOpacity,
				listBarMode, listOpacity, listHeight, lowercased, modeTags, flipHorizontally, /*flipVertically,*/ hideVisual, flipTags, colorMode, red, green, blue, red1, green1, blue1, rainbow, mcFont
		);

	}

	public void onEvent(Event e) {
		if(e instanceof EventRender && e.isPre()) {
			String text1 = text
					.replace("{fps}", String.valueOf(Minecraft.getDebugFPS()))
					.replace("{time}", new SimpleDateFormat("kk:mm").format(new Date()))
					.replace("{mcversion}", Minecraft.getMinecraft().getVersion());

			// remove the stupid space at the end thanks to the args in the command system.
			if (text1.charAt(text1.length() - 1) == ' ')
				text1.replace(text1.substring(text1.length() - 1),  "");

			drawWatermark(text1);

			int i = 0,
			i2 = 0,
			height = (int)listHeight.getValue();

			for(Module m : Wrapper.getModuleManager().getModules()) {
				if(m.isEnabled()) {
					if(hideVisual.isEnabled() && m.getCategory() == Category.VISUAL)
						continue;
					String sep = (modeTags.isEnabled() ? (m.getTag() != "" ? " ": "") + m.getTag() : "");

					String modText = m.getName() + (!mcFont.isEnabled() ? "&7" : "\u00a77") + sep;

					if(flipTags.isEnabled())
						modText = (!mcFont.isEnabled() ? "&7" : "\u00a77") + (modeTags.isEnabled() ? m.getTag() + (m.getTag() != "" ? " ": "") : "") + (!mcFont.isEnabled() ? "&r" : "\u00a7r") + m.getName() + (!mcFont.isEnabled() ? "&f" : "\u00a7r");

					Wrapper.getModuleManager().getModules().sort(Comparator.comparingInt(mod -> lowercased.isEnabled() ? (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth((((Module)mod).getName() + (modeTags.isEnabled() ? ((Module)mod).getTag2() : "")).toLowerCase()) : font.getStringWidth((((Module)mod).getName() + (modeTags.isEnabled() ? ((Module)mod).getTag2() : "")).toLowerCase())) : mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(((Module)mod).getName() + (modeTags.isEnabled() ? ((Module)mod).getTag2() : "")) : font.getStringWidth(((Module)mod).getName() + (modeTags.isEnabled() ? ((Module)mod).getTag2() : ""))).reversed());

					if(lowercased.isEnabled())
						modText = modText.toLowerCase();

					if(flipHorizontally.isEnabled()) {
						if (mcFont.isEnabled()) {
							Gui.drawRect(x + width, y + i, x + width + mc.fontRendererObj.getStringWidth(modText) + 5, y + 9 + i + height, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());
							mc.fontRendererObj.drawStringWithShadow(modText, x + width + 2, y + i + (float) height / 2 + 1, getColor(i2));
						} else {
							Gui.drawRect(x + width, y + i, x + width + font.getStringWidth(modText) + 4, 9 + y + i + height, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());
							font.drawStringWithShadow(modText, x + width + 2, y + i + (float) height / 2 + 1, getColor(i2));
						}
					} else {
						if (mcFont.isEnabled()) {
							Gui.drawRect(x + width, y + i, x + width - mc.fontRendererObj.getStringWidth(modText) - 5, y + 9 + i + height, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());
							mc.fontRendererObj.drawStringWithShadow(modText, x + width - mc.fontRendererObj.getStringWidth(modText) - 2, y + i + (float) height / 2 + 1, getColor(i2));
						} else {
							Gui.drawRect(x + width, y + i, x + width - font.getStringWidth(modText) - 3, 9 + y + i + height, new Color(0, 0, 0, (int) listOpacity.getValue()).getRGB());
							font.drawStringWithShadow(modText, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) : font.getStringWidth(modText)) - 1, y + i + (float) height / 2 + 1, getColor(i2));
						}
					}

					switch (listBarMode.getMode()) {
						case "Front":
							if(flipHorizontally.isEnabled())
								Gui.drawRect(x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + i, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 6 : font.getStringWidth(modText) + 5), y + 9 + i + height, getColor(i2));
							else
								Gui.drawRect(x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 3), y + i, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + 9 + i + height, getColor(i2));
							break;
						case "Back":
							if(flipHorizontally.isEnabled())
								Gui.drawRect(x + width - 1, y + i, x + width, 9 + y + i + height, getColor(i2));
							else
								Gui.drawRect(x + width, y + i, x + width + 1, 9 + y + i + height, getColor(i2));
							break;
						case "Back2":
							if(flipHorizontally.isEnabled())
								Gui.drawRect(x + width - 1, y + i + 1, x + width, 8 + y + i + height, getColor(i2));
							else
								Gui.drawRect(x + width, y + i + 1, x + width + 1, 8 + y + i + height, getColor(i2));
							break;
						case "Top":
							/*if (!flipVertically.isEnabled()) {*/
								if (flipHorizontally.isEnabled())
									Gui.drawRect(x + width, y, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y - 1, getColor(i2));
								else
									Gui.drawRect(x + width, y, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 3), y - 1, getColor(i2));
							//}
							break;

							// Messiest shit ive made LMFAOO
						case "Outline":
							if(flipHorizontally.isEnabled())
								Gui.drawRect(x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + i, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 6 : font.getStringWidth(modText) + 5), y + 10 + i + height, getColor(i2));
							else
								Gui.drawRect(x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 3), y + i, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + 10 + i + height, getColor(i2));

							if(flipHorizontally.isEnabled())
								Gui.drawRect(x + width - 1, y + i, x + width, 9 + y + i + height, getColor(i2));
							else
								Gui.drawRect(x + width, y + i, x + width + 1, 9 + y + i + height, getColor(i2));

							ArrayList<Module> enabled = new ArrayList<>(Wrapper.getModuleManager().getModules());
							enabled.removeIf(module -> !module.isEnabled() || (hideVisual.isEnabled() && module.getCategory() == Category.VISUAL));
							int index = enabled.indexOf(m);

							String sep1 = (modeTags.isEnabled() ? (enabled.get(0).getTag() != "" ? " ": "") + enabled.get(0).getTag() : "");
							String modText1 = enabled.get(0).getName() +  (!mcFont.isEnabled() ? "&7" : "\u00a77") + sep1;

							if(lowercased.isEnabled())
								modText1 = modText1.toLowerCase();

							if(!flipHorizontally.isEnabled())
								Gui.drawRect(x + width + 1, y, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText1) + 5 : font.getStringWidth(modText1) + 4), y - 1, getColor(i2));
							else
								Gui.drawRect(x + width - 1, y, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText1) + 5 : font.getStringWidth(modText1) + 4), y + 1, getColor(i2));

							if(index != enabled.size() - 1) {
								String sep2 = (modeTags.isEnabled() ? (enabled.get(index + 1).getTag() != "" ? " ": "") + enabled.get(index + 1).getTag() : "");
								String modText2 = enabled.get(index + 1).getName() +  (!mcFont.isEnabled() ? "&7" : "\u00a77") + sep2;

								if(lowercased.isEnabled())
									modText2 = modText2.toLowerCase();

								if(flipHorizontally.isEnabled())
									Gui.drawRect(x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText2) + 5 : font.getStringWidth(modText2) + 4), y + i + font.getHeight() + height + 1, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 5 : font.getStringWidth(modText) + 4), y + i + font.getHeight() + height + 2, getColor(i2));
								else
									Gui.drawRect(x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText2) + 4 : font.getStringWidth(modText2) + 3), y + i + font.getHeight() + height + 1, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText) + 4 : font.getStringWidth(modText) + 3), y + i + font.getHeight() + height + 2, getColor(i2));
							} else {
								String sep3 = (modeTags.isEnabled() ? (enabled.get(index).getTag() != "" ? " ": "") + enabled.get(index).getTag() : "");
								String modText3 = enabled.get(index).getName() +  (!mcFont.isEnabled() ? "&7" : "\u00a77") + sep3;

								if(lowercased.isEnabled())
									modText3 = modText3.toLowerCase();

								if(!flipHorizontally.isEnabled())
									Gui.drawRect(x + width + 1, y + i + 9 + height, x + width - (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText3) + 5 : font.getStringWidth(modText3) + 4), y + i + 10 + height, getColor(i2));
								else
									Gui.drawRect(x + width - 1, y + i + 9 + height, x + width + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(modText3) + 6 : font.getStringWidth(modText3) + 5), y + i + 10 + height, getColor(i2));

							}
							break;
					}
					/*if(flipVertically.isEnabled())
						i -= 9 + height;
					else*/
						i += 9 + height;

					i2 += 9;
				}
			}
		}
	}

	private void drawWatermark(String text) {
		switch(watermarkMode.getMode()) {
			case "Simple":
				if(mcFont.isEnabled())
					mc.fontRendererObj.drawStringWithShadow(text, 2, 2, getColor((long)rainbow.getValue()));
				else
					font.drawStringWithShadow(text, 2, 2, getColor((long)rainbow.getValue()));
				break;
			case "Cool":
				if(mcFont.isEnabled()) {
					Gui.drawRect(2, 2, 3 + mc.fontRendererObj.getStringWidth(text), 15, new Color(0, 0, 0, (int)watermarkOpacity.getValue()).getRGB());
					Gui.drawRect(2, 2, 3 + mc.fontRendererObj.getStringWidth(text), 3, getColor((long)rainbow.getValue()));
					mc.fontRendererObj.drawStringWithShadow(text, 5, 5, -1);
				} else {
					Gui.drawRect(2, 2, font.getStringWidth(text) + 6, 15, new Color(0, 0, 0, (int)watermarkOpacity.getValue()).getRGB());
					Gui.drawRect(2, 2, font.getStringWidth(text) + 6, 3, getColor((long)rainbow.getValue()));
					font.drawStringWithShadow(text, 5, 6, -1);
				}
				break;
			case "Sense":
				Gui.drawRect(3.5, 3.5, 3.5 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 4 : font.getStringWidth(text) + 5), 20.5, 0xFF3D3F45);
				Gui.drawRect(5, 5, 4 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 2 : font.getStringWidth(text) + 3), 19, 0xFF1F201C);
				Gui.drawRect(5, 4.5, 4 + (mcFont.isEnabled() ? mc.fontRendererObj.getStringWidth(text) + 2 : font.getStringWidth(text) + 3), 5.5, getColor((int)rainbow.getValue()));
				if(mcFont.isEnabled())
					mc.fontRendererObj.drawStringWithShadow(text, 7.5f, 8.5f, getColor((int)rainbow.getValue()));
				else
					font.drawStringWithShadow(text, 7f, 8.5f, getColor((int)rainbow.getValue()));
				break;
		}
	}

	public static void setWatermark(String watermark) {
		text = watermark;
		save();

	}

	public int getColor(long wave) {
		Color
		custom = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()),
		custom1 = new Color((int) red1.getValue(), (int) green1.getValue(), (int) blue1.getValue());
		double offset = (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (wave * 49.8) / rainbow.getValue() / 50;

		switch (colorMode.getMode()) {
		case "Custom":
			return custom.getRGB();
		case "Rainbow":
			return ColorUtil.getRainbow(4, 0.5f, 1f, wave * 10);
		case "Fade":
			return ColorUtil.getGradient(custom, custom.darker().darker(), offset);
		case "Gradient":
			return ColorUtil.getGradient(custom, custom1, offset);
		}
		return -1;
	}
}
