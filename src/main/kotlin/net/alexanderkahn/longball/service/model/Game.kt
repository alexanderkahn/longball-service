package net.alexanderkahn.longball.service.model

import java.time.ZonedDateTime

data class Game(val awayTeam: Team, val homeTeam: Team, val startTime: ZonedDateTime)