package client.rapid.util.font;

import java.awt.Font;

import client.rapid.util.console.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts {
	public static MCFontRenderer
	normal = getFont("font.ttf", 21),
	normal2 = getFont("font.ttf", 19),
	inter = getFont("inter.ttf", 19),
	greycliff = getFont("greycliff.ttf", 19),
	sfui = getFont("sfui.ttf", 19),
	clickgui = getFont("font.ttf", 17),
	newIcons = getFont("category.ttf", 22),
	newIconsBig = getFont("category.ttf", 40);

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
