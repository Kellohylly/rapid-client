package client.rapid.util;

public class MathUtil {

    public static boolean isInPercentage(double value) {
        return (int) randomNumber(1, 10) < value;
    }
	
    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }

}

