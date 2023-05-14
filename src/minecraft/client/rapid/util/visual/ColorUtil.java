package client.rapid.util.visual;

import java.awt.Color;

public class ColorUtil {
	
	public static int getRainbow(float seconds, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (seconds * 1000);
		return Color.HSBtoRGB(hue, 0.7f, 0.86f);
	}

    public static int getGradient(Color first, Color second, double index) {
        if (index > 1)
        	index = (int)index % 2 == 0 ? index % 1 : 1 - index % 1;
        
        return new Color(
		(int) (first.getRed() * (1 - index) + second.getRed() * index),
		(int) (first.getGreen() * (1 - index) + second.getGreen() * index),
		(int) (first.getBlue() * (1 - index) + second.getBlue() * index)).getRGB();
    }

}
