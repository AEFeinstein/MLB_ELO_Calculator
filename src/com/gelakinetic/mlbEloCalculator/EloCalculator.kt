package com.gelakinetic.mlb_elo_calculator

object EloCalculator {
    /*
     * Math from:
     * https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/
     */
    private const val K = 32.0
    const val STARTING_ELO = 1600.0

    internal fun getEloDelta(elo1: Double, score1: Int, elo2: Double, score2: Int): Double {
        return K * (S(score1, score2) - E(elo1, elo2))
    }

    private fun R(rating: Double): Double {
        return Math.pow(10.0, rating / 400)
    }

    private fun E(rating1: Double, rating2: Double): Double {
        return R(rating1) / (R(rating1) + R(rating2))
    }

    private fun S(ourScore: Int, otherScore: Int): Double {
        if (ourScore > otherScore) {
            return 1.0
        } else if (ourScore < otherScore) {
            return 0.0
        }
        return 0.5
    }
}
