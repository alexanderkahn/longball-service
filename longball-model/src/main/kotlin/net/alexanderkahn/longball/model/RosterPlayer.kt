package net.alexanderkahn.longball.model

import java.time.ZonedDateTime
import java.util.*

data class RosterPlayer (val id: UUID, val team: UUID, val player: UUID, val jerseyNumber: Number, val startDate: ZonedDateTime, val endDate: ZonedDateTime? = null)