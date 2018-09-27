package com.gelakinetic.mlb_elo_calculator;

public class EloCalculator {
    /*
     * Math from:
     * https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/
     */
    private static final double K = 32;
    public static final double STARTING_ELO = 1600;

    static double getEloDelta(double elo1, int score1, double elo2, int score2) {
        return (K * (S(score1, score2) - E(elo1, elo2)));
    }

    private static double R(double rating) {
        return Math.pow(10, rating / 400);
    }

    private static double E(double rating1, double rating2) {
        return (R(rating1) / (R(rating1) + R(rating2)));
    }

    private static double S(int ourScore, int otherScore) {
        if (ourScore > otherScore) {
            return 1;
        }
        else if (ourScore < otherScore) {
            return 0;
        }
        return 0.5;
    }
}
