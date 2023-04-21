package client.rapid.util.font;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import client.rapid.util.Logger;

public class MCFontRenderer extends CFont {
	protected CFont.CharData[] boldChars = new CFont.CharData[256], italicChars = new CFont.CharData[256], boldItalicChars = new CFont.CharData[256];
  	protected DynamicTexture texBold, texItalic, texItalicBold;
  	private final int[] colorCode = new int[32];
  
  	public MCFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
  		super(font, antiAlias, fractionalMetrics);
  		setupMCColorCodes();
  		setupBoldItalicIDs();
  	}
  
  	public float drawStringWithShadow(String text, double x, double y, int color) {
  		float shadowWidth = drawString(text, x + 0.74D, y + 0.7D, color, true);
  		return Math.max(shadowWidth, drawString(text, x, y, color, false));
  	}
  
  	public float drawString(String text, float x, float y, int color) {
  		return drawString(text, x, y, color, false);
  	}
  
  	public float drawCenteredString(String text, float x, float y, int color) {
  		return drawString(text, x - (getStringWidth(text) / 2), y, color);
  	}
  
  	public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
  		float shadowWidth = drawString(text, (x - (getStringWidth(text) / 2)) + 0.6D, y + 0.6D, color, true);
  		return drawString(text, x - (getStringWidth(text) / 2), y, color);
  	}
  
  	public float drawString(String text, double x, double y, int color, boolean shadow) {
  		x--;
  		if (text == null)
  			return 0.0F; 
  		
  		if (color == 553648127)
  			color = 16777215; 
  		
  		if ((color & 0xFC000000) == 0)
  			color |= 0xFF000000; 
  		
  		if (shadow)
  			color = (new Color(color)).darker().darker().darker().getRGB(); 
  		
  		CFont.CharData[] currentData = this.charData;
  		float alpha = (color >> 24 & 0xFF) / 255.0F;
  		boolean bold = false, italic = false, strikethrough = false, underline = false;
  		x *= 2.0D;
  		y = (y - 3.0D) * 2.0D;
  		GL11.glPushMatrix();
  		GlStateManager.scale(0.5D, 0.5D, 0.5D);
  		GlStateManager.enableBlend();
  		GlStateManager.blendFunc(770, 771);
  		GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
  		int size = text.length();
  		GlStateManager.enableTexture2D();
  		GlStateManager.bindTexture(this.tex.getGlTextureId());
    	GL11.glBindTexture(3553, this.tex.getGlTextureId());
    	for (int i = 0; i < size; i++) {
    		char character = text.charAt(i);
    		if (character == '&' || character == '\u00a7') {
    			int colorIndex = 21;
        	try {
        		colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
        	} catch (Exception e) {
        		Logger.error("Something went wrong: " + e.getMessage());
        	} 
        	if (colorIndex < 16) {
        		bold = false;
        		italic = false;
        		underline = false;
        		strikethrough = false;
        		GlStateManager.bindTexture(this.tex.getGlTextureId());
        		currentData = this.charData;
        		
        		if (colorIndex < 0)
        			colorIndex = 15; 
        		
        		if (shadow)
        			colorIndex += 16; 
        		
        		int colorcode = this.colorCode[colorIndex];
        		GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
        	} else if (colorIndex == 17) {
        		bold = true;
        		if (italic) {
        			GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
        			currentData = this.boldItalicChars;
        		} else {
        			GlStateManager.bindTexture(this.texBold.getGlTextureId());
        			currentData = this.boldChars;
        		} 
        	} else if (colorIndex == 18) {
        		strikethrough = true;
        	} else if (colorIndex == 19) {
        		underline = true;
        	} else if (colorIndex == 20) {
        		italic = true;
        		if (bold) {
        			GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
        			currentData = this.boldItalicChars;
        		} else {
        			GlStateManager.bindTexture(this.texItalic.getGlTextureId());
        			currentData = this.italicChars;
        		} 
        	} else {
        		bold = false;
        		italic = false;
        		underline = false;
        		strikethrough = false;
        		GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
        		GlStateManager.bindTexture(this.tex.getGlTextureId());
        		currentData = this.charData;
        	} 
        	i++;
    		} else if (character < currentData.length) {
    			GL11.glBegin(4);
    			drawChar(currentData, character, (float)x, (float)y);
    			GL11.glEnd();
    			
        	if (strikethrough)
        		drawLine(x, y + ((currentData[character]).height / 2), x + (currentData[character]).width - 8.0D, y + ((currentData[character]).height / 2), 1.0F); 
        	
        	if (underline)
        		drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F); 
        	
        	x += ((currentData[character]).width - 8 + this.charOffset);
    		} 
    	}
		GL11.glHint(3155, 4352);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		return (float)x / 2.0F;
  }
  
  	public int getStringWidth(String text) {
  		if (text == null)
  			return 0; 
  		
  		int width = 0;
  		CFont.CharData[] currentData = this.charData;
  		int size = text.length();
  		for (int i = 0; i < size; i++) {
  			char character = text.charAt(i);
  			if (character == '&') {
  				i++;
  			} else if (character < currentData.length) {
  				width += (currentData[character]).width - 8 + this.charOffset;
  			} 
  		} 
  		return width / 2;
  	}
  
  	public void setFont(Font font) {
  		super.setFont(font);
  		setupBoldItalicIDs();
  	}
  
  	public void setAntiAlias(boolean antiAlias) {
  		super.setAntiAlias(antiAlias);
  		setupBoldItalicIDs();
  	}
  
  	public void setFractionalMetrics(boolean fractionalMetrics) {
  		super.setFractionalMetrics(fractionalMetrics);
  		setupBoldItalicIDs();
  	}
  
  	private void setupBoldItalicIDs() {
  		this.texBold = setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
  		this.texItalic = setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
  		this.texItalicBold = setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
  	}
  
  	private void drawLine(double x, double y, double x1, double y1, float width) {
  		GL11.glDisable(3553);
  		GL11.glLineWidth(width);
    	GL11.glBegin(1);
    	GL11.glVertex2d(x, y);
    	GL11.glVertex2d(x1, y1);
    	GL11.glEnd();
    	GL11.glEnable(3553);
  	}
  
  	public List<String> wrapWords(String text, double width) {
  		List<String> finalWords = new ArrayList();
  		if (getStringWidth(text) > width) {
  			String[] words = text.split(" "), arrayOfString1;
  			String currentWord = "";
  			char lastColorCode = Character.MAX_VALUE;
  			byte b;
  			int i;
  			for (i = (arrayOfString1 = words).length, b = 0; b < i; ) {
  				String word = arrayOfString1[b];
  				for (int j = 0; j < (word.toCharArray()).length; j++) {
  					char c = word.toCharArray()[j];
  					if (c == '&' && j < (word.toCharArray()).length - 1)
  						lastColorCode = word.toCharArray()[j + 1]; 
  				} 
  				if (getStringWidth(String.valueOf(currentWord) + word + " ") < width) {
  					currentWord = String.valueOf(currentWord) + word + " ";
  				} else {
  					finalWords.add(currentWord);
  					currentWord = "§" + lastColorCode + word + " ";
  				} 
  				b++;
  			} 
  			if (currentWord.length() > 0)
  				if (getStringWidth(currentWord) < width) {
  					finalWords.add("§" + lastColorCode + currentWord + " ");
  				} else {
  					finalWords.addAll(formatString(currentWord, width));
  				}  
  		} else {
  			finalWords.add(text);
  		} 
  		return finalWords;
  	}
  
  	public List<String> formatString(String string, double width) {
  		List<String> finalWords = new ArrayList<>();
  		String currentWord = "";
  		char lastColorCode = Character.MAX_VALUE;
  		char[] chars = string.toCharArray();
  		for (int i = 0; i < chars.length; i++) {
  			char c = chars[i];
  			if (c == '&' && i < chars.length - 1)
  				lastColorCode = chars[i + 1]; 
  			
  			if (getStringWidth(String.valueOf(currentWord) + c) < width) {
  				currentWord = String.valueOf(currentWord) + c;
  			} else {
  				finalWords.add(currentWord);
  				currentWord = "§" + lastColorCode + c;
  			} 
  		} 
  		if (currentWord.length() > 0)
  			finalWords.add(currentWord); 
  		return finalWords;
  	}
  
  	private void setupMCColorCodes() {
  		for (int index = 0; index < 32; index++) {
  			int a = (index >> 3 & 0x1) * 85;
  			int RED = (index >> 2 & 0x1) * 170 + a;
  			int GREEN = (index >> 1 & 0x1) * 170 + a;
  			int BLEUE = (index & 0x1) * 170 + a;
  			if (index == 6)
  				RED += 85; 
  			if (index >= 16) {
  				RED /= 4;
  				GREEN /= 4;
  				BLEUE /= 4;
  			} 
  			this.colorCode[index] = (RED & 0xFF) << 16 | (GREEN & 0xFF) << 8 | BLEUE & 0xFF;
  		} 
  	}
}
