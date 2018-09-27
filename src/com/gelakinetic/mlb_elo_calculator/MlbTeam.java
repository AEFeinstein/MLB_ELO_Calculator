package com.gelakinetic.mlb_elo_calculator;

import java.util.Comparator;

public class MlbTeam implements Comparable<MlbTeam> {

    String name;
    int wins;
    int losses;
    double elo;
    public int winLossRank;
    public int eloRank;

    MlbTeam(String n) {
        name = n;
        wins = 0;
        losses = 0;
        elo = EloCalculator.STARTING_ELO;
    }

    @Override
    public String toString() {
        return String.format("%-21s (%3d-%3d) ELO: %d", name, wins, losses, (int) Math.round(elo));
    }

    @Override
    public int compareTo(MlbTeam o) {
        return -1 * Double.compare(this.elo, o.elo);
    }

    static class MlbTeamWinsComparator implements Comparator<MlbTeam> {

        @Override
        public int compare(MlbTeam arg0, MlbTeam arg1) {
            return -1 * Integer.compare(arg0.wins, arg1.wins);
        }
    }

    static class MlbTeamRankChangeComparator implements Comparator<MlbTeam> {

        @Override
        public int compare(MlbTeam o1, MlbTeam o2) {
            return Integer.compare((o2.winLossRank - o2.eloRank), (o1.winLossRank - o1.eloRank));
        }

    }

}