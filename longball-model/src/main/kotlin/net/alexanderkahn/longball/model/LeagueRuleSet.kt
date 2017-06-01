package net.alexanderkahn.longball.model

//For now, this is a static class. Eventually if there's reason to expand it it might make sense to persist per-league
class LeagueRuleSet {
    companion object {
        val  BATTERS_PER_LINEUP = 9
        val STRIKES_PER_OUT = 3
        val BALLS_PER_WALK = 4
        val OUTS_PER_INNING = 3
        val INNINGS_PER_GAME = 9
    }
}