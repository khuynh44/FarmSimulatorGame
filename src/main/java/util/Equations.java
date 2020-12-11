package main.java.util;

public class Equations {
    private Equations() {
    }

    public static double gaussian(double x, double mu, double sigma) {
        final double normalizer = 1 / (sigma * Math.sqrt(2 * Math.PI));
        final double result =
            normalizer * Math.pow(Math.E, -1 / 2.0 * Math.pow((x - mu) / sigma, 2));

        return result;
    }

    public static double weighWithModifiers(double x, double modifier) {
        return (1 + (modifier / (double) 20)) * x;
    }
}
