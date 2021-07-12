package com.gelakinetic.mlbEloCalculator

import java.util.Comparator
import kotlin.math.roundToInt

class MlbTeam internal constructor(internal var name: String) : Comparable<MlbTeam> {
    internal var wins = 0
    internal var losses = 0
    internal var elo = EloCalculator.STARTING_ELO
    internal var winLossRank = 0
    internal var eloRank = 0

    override fun toString(): String {
        return String.format("%-21s (%3d-%3d) ELO: %d", name, wins, losses, elo.roundToInt())
    }

    override fun compareTo(other: MlbTeam): Int {
        return -1 * this.elo.compareTo(other.elo)
    }

    internal class MlbTeamEloComparator : Comparator<MlbTeam> {
        override fun compare(arg0: MlbTeam, arg1: MlbTeam): Int {
            return -1 * arg0.elo.compareTo(arg1.elo)
        }
    }

    internal class MlbTeamWinsComparator : Comparator<MlbTeam> {
        override fun compare(arg0: MlbTeam, arg1: MlbTeam): Int {
            return -1 * arg0.wins.compareTo(arg1.wins)
        }
    }

    internal class MlbTeamRankChangeComparator : Comparator<MlbTeam> {
        override fun compare(o1: MlbTeam, o2: MlbTeam): Int {
            return (o2.winLossRank - o2.eloRank).compareTo(o1.winLossRank - o1.eloRank)
        }
    }
}