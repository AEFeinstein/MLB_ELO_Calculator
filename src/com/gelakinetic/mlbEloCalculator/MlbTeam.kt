package com.gelakinetic.mlbEloCalculator

import java.util.Comparator

class MlbTeam internal constructor(internal var name: String) : Comparable<MlbTeam> {
    internal var wins = 0
    internal var losses = 0
    internal var elo = EloCalculator.STARTING_ELO
    internal var winLossRank = 0
    internal var eloRank = 0

    override fun toString(): String {
        return String.format("%-21s (%3d-%3d) ELO: %d", name, wins, losses, Math.round(elo).toInt())
    }

    override fun compareTo(other: MlbTeam): Int {
        return -1 * java.lang.Double.compare(this.elo, other.elo)
    }

    internal class MlbTeamEloComparator : Comparator<MlbTeam> {
        override fun compare(arg0: MlbTeam, arg1: MlbTeam): Int {
            return -1 * java.lang.Double.compare(arg0.elo, arg1.elo)
        }
    }

    internal class MlbTeamWinsComparator : Comparator<MlbTeam> {
        override fun compare(arg0: MlbTeam, arg1: MlbTeam): Int {
            return -1 * Integer.compare(arg0.wins, arg1.wins)
        }
    }

    internal class MlbTeamRankChangeComparator : Comparator<MlbTeam> {
        override fun compare(o1: MlbTeam, o2: MlbTeam): Int {
            return Integer.compare(o2.winLossRank - o2.eloRank, o1.winLossRank - o1.eloRank)
        }
    }
}