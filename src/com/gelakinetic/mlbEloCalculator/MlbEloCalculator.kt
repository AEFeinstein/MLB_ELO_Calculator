package com.gelakinetic.mlb_elo_calculator

import org.jsoup.Jsoup
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

object MLB_ELO_Calculator {

    @JvmStatic
    fun main(args: Array<String>) {

        val teams = ArrayList<MlbTeam>(30)

        // Pattern matches line scores like "Colorado Rockies (2) @ Arizona D'Backs (8)"
        val pattern = Pattern.compile("(.*) \\(([0-9]+)\\) @ (.*) \\(([0-9]+)\\)")

        try {
            // Read the baseball-reference webpage for all 2018 scores, then for each game
            for (game in Jsoup.connect("https://www.baseball-reference.com/leagues/MLB/2018-schedule.shtml").get().getElementsByAttributeValue("class", "game")) {
                // Try to match it against the regular expression
                val matcher = pattern.matcher(game.text())
                if (matcher.find()) {
                    // If it matches, pull out the team names and scores
                    val team1: String = matcher.group(1)
                    val score1: Int = Integer.parseInt(matcher.group(2))
                    val team2: String = matcher.group(3)
                    val score2: Int = Integer.parseInt(matcher.group(4))

                    // And process the names and scores
                    processGame(findOrCreateTeam(team1, teams), score1, findOrCreateTeam(team2, teams), score2)
                } else {
                    // Probably a game that has yet to be played
                    // System.err.println("Unmatched: " + headline.text());
                }
            }

            // Print out the teams, sorted by W-L
            println("W-L Rankings")
            println("------------")
            teams.sortWith(MlbTeam.MlbTeamWinsComparator())
            var winLossRank = 1
            for (team in teams) {
                team.winLossRank = winLossRank++
                println(team)
            }

            print("\n=============\n\n")

            // Print out the teams sorted by ELO
            println("ELO Rankings")
            println("------------")
            teams.sortWith(MlbTeam.MlbTeamEloComparator())
            var eloRank = 1
            for (team in teams) {
                team.eloRank = eloRank++
                println(team)
            }

            print("\n=============\n\n")

            // Print out the teams, sorted by difference between ELO and W-L ranks
            println("W-L to ELO Rank Change")
            println("----------------------")
            teams.sortWith(MlbTeam.MlbTeamRankChangeComparator())
            for (team in teams) {
                println(String.format("%-21s: %+3d", team.name, team.winLossRank - team.eloRank))
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

    }

    private fun findOrCreateTeam(teamName: String, teams: ArrayList<MlbTeam>): MlbTeam {
        // First look for a team
        for (team in teams) {
            if (team.name == teamName) {
                return team
            }
        }

        // If it isn't found, create a new one
        val mlbTeam = MlbTeam(teamName)
        teams.add(mlbTeam)
        return mlbTeam
    }

    private fun processGame(mlbTeam1: MlbTeam, score1: Int, mlbTeam2: MlbTeam, score2: Int) {
        // Calculate the change in ELO, and apply it
        val delta = EloCalculator.getEloDelta(mlbTeam1.elo, score1, mlbTeam2.elo, score2)
        mlbTeam1.elo += delta
        mlbTeam2.elo -= delta

        // Tally W-L too
        if (score1 > score2) {
            mlbTeam1.wins++
            mlbTeam2.losses++
        } else {
            mlbTeam1.losses++
            mlbTeam2.wins++
        }
    }
}
