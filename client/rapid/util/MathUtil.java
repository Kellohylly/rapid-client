package client.rapid.util;

public class MathUtil {
	
    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }
}

