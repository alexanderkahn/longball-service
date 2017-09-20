package net.alexanderkahn.longball.model

import java.time.OffsetDateTime
import java.util.*

data class Game(val id: UUID, val league: UUID, val awayTeam: UUID, val homeTeam: UUID, val startTime: OffsetDateTime)