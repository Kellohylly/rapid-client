package client.rapid.util;

public class MathUtil {
    public static boolean isinpercentage(double valoue) {
        int ch = (int) randomNumber(1, 10);
        return ch < valoue;
    }
	
    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }
}

