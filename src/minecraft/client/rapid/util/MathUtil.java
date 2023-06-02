package client.rapid.util;

public class MathUtil {

    public static boolean isInPercentage(double value) {
        return randomNumber(1, 10) < value;
    }

    // Get a random number between max and min
    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }

}

