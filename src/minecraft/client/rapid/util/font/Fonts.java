package client.rapid.util.font;

import java.awt.Font;

import client.rapid.util.console.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts {
	public static MCFontRenderer
	normal = getFont("font.ttf", 21.0F),
	normal2 = getFont("font.ttf", 19.0F),
	inter = getFont("inter.ttf", 19.0F),
	sfui = getFont("sfui.ttf", 19.0F),
	normal3 = getFont("font.ttf", 28.0F),
	clickgui = getFont("font.ttf", 17.0F),
	small2 = getFont("font.ttf", 12F),
	icons3 = getFont("icons.ttf", 26F),
	icons4 = getFont("icons1.ttf", 26F),
	icons4Big = getFont("icons1.ttf", 40F),
	icons5 = getFont("icons2.ttf", 26F),
	icons5Big = getFont("icons2.ttf", 40F);

	private static MCFontRenderer getFont(String location, float size) {
		return new MCFontRenderer(getTrueType(location, size), true, true);
	}

	public static Font getTrueType(String fontLocation, float fontSize) {
		Font output = null;
		try {
			output = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("rapid/fonts/" + fontLocation)).getInputStream());
			output = output.deriveFont(fontSize);
		} catch (Exception e) {
			Logger.error("Something went wrong: " + e.getMessage());
		} 
		return output;
	}
}
