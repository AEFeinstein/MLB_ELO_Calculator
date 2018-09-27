package com.gelakinetic.mlb_elo_calculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MLB_ELO_Calculator {

    public static void main(String args[]) {

        ArrayList<MlbTeam> teams = new ArrayList<MlbTeam>(30);

        // Pattern matches line scores like "Colorado Rockies (2) @ Arizona D'Backs (8)"
        Pattern pattern = Pattern.compile("(.*) \\(([0-9]+)\\) @ (.*) \\(([0-9]+)\\)");

        try {
            // Read the baseball-reference webpage for all 2018 scores
            Document doc = Jsoup.connect("https://www.baseball-reference.com/leagues/MLB/2018-schedule.shtml").get();
            // For each game
            for (Element game : doc.getElementsByAttributeValue("class", "game")) {
                // Try to match it against the regular expression
                Matcher matcher = pattern.matcher(game.text());
                if (matcher.find()) {
                    // If it matches, pull out the team names and scores
                    String team1 = matcher.group(1);
                    int score1 = Integer.parseInt(matcher.group(2));
                    String team2 = matcher.group(3);
                    int score2 = Integer.parseInt(matcher.group(4));

                    // And process the names and scores
                    processGame(findOrCreateTeam(team1, teams), score1, findOrCreateTeam(team2, teams), score2);
                }
                else {
                    // Probably a game that has yet to be played
                    // System.err.println("Unmatched: " + headline.text());
                }
            }

            // Print out the teams, sorted by W-L
            // Print out the teams sorted by ELO
            System.out.println("W-L Rankings");
            System.out.println("------------");
            Collections.sort(teams, new MlbTeam.MlbTeamWinsComparator());
            int winLossRank = 1;
            for (MlbTeam team : teams) {
                team.winLossRank = winLossRank++;
                System.out.println(team);
            }

            System.out.print("\n=============\n\n");

            // Print out the teams sorted by ELO
            System.out.println("ELO Rankings");
            System.out.println("------------");
            Collections.sort(teams);
            int eloRank = 1;
            for (MlbTeam team : teams) {
                team.eloRank = eloRank++;
                System.out.println(team);
            }

            System.out.print("\n=============\n\n");

            System.out.println("W-L to ELO Rank Change");
            System.out.println("----------------------");
            Collections.sort(teams, new MlbTeam.MlbTeamRankChangeComparator());
            for (MlbTeam team : teams) {
                System.out.println(String.format("%-21s: %+3d", team.name, team.winLossRank - team.eloRank));
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static MlbTeam findOrCreateTeam(String teamName, ArrayList<MlbTeam> teams) {
        // First look for a team
        for (MlbTeam team : teams) {
            if (team.name.equals(teamName)) {
                return team;
            }
        }

        // If it isn't found, create a new one
        MlbTeam mlbTeam = new MlbTeam(teamName);
        teams.add(mlbTeam);
        return mlbTeam;
    }

    private static void processGame(MlbTeam mlbTeam1, int score1, MlbTeam mlbTeam2, int score2) {
        // Calculate the change in ELO, and apply it
        double delta = EloCalculator.getEloDelta(mlbTeam1.elo, score1, mlbTeam2.elo, score2);
        mlbTeam1.elo += delta;
        mlbTeam2.elo -= delta;

        // Tally W-L too
        if (score1 > score2) {
            mlbTeam1.wins++;
            mlbTeam2.losses++;
        }
        else {
            mlbTeam1.losses++;
            mlbTeam2.wins++;
        }
    }
}
