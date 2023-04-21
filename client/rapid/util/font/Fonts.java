package client.rapid.util.font;

import java.awt.Font;

import client.rapid.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts {
	public static final MCFontRenderer
	normal = new MCFontRenderer(getTrueType("font.ttf", 21.0F, 0), true, true),
	normal2 = new MCFontRenderer(getTrueType("font.ttf", 19.0F, 0), true, true),
	normal3 = new MCFontRenderer(getTrueType("font.ttf", 28.0F, 0), true, true),
	clickgui = new MCFontRenderer(getTrueType("font.ttf", 17.0F, 0), true, true),
	small2 = new MCFontRenderer(getTrueType("font.ttf", 12F, 0), true, true),
	icons3 = new MCFontRenderer(getTrueType("icons.ttf", 26F, 0), true, true),
	icons4 = new MCFontRenderer(getTrueType("icons1.ttf", 26F, 0), true, true),
	icons4Big = new MCFontRenderer(getTrueType("icons1.ttf", 40F, 0), true, true),
	icons5 = new MCFontRenderer(getTrueType("icons2.ttf", 26F, 0), true, true),
	icons5Big = new MCFontRenderer(getTrueType("icons2.ttf", 40F, 0), true, true);

	private static Font getTrueType(String fontLocation, float fontSize, int fontType) {
		Font output = null;
		try {
			output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("rapid/fonts/" + fontLocation)).getInputStream());
			output = output.deriveFont(fontSize);
		} catch (Exception e) {
			Logger.error("Something went wrong: " + e.getMessage());
		} 
		return output;
	}
}
