package net.alexanderkahn.longball.model.dto

data class LeagueRuleSetDTO(
        val battersPerLineup: Int = 9,
        val strikesPerOut: Int = 3,
        val ballsPerWalk: Int = 4,
        val outsPerInning: Int = 3,
        val inningsPerGame: Int = 9,
        val strikeOutOnFoul: Boolean = false,
        val allowPinchHitter: Boolean = false,
        val allowExtraInnings: Boolean = true
)

