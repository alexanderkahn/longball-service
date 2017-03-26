package net.alexanderkahn.longball.service.model

import java.time.ZonedDateTime

data class Game(val id: Long, val league: Long, val awayTeam: Long, val homeTeam: Long, val startTime: ZonedDateTime)