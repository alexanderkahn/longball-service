package net.alexanderkahn.longball.model

import java.time.OffsetDateTime
import java.util.*

data class Player(val id: UUID, val team: UUID, val player: UUID, val jerseyNumber: Number, val startDate: OffsetDateTime, val endDate: OffsetDateTime? = null)