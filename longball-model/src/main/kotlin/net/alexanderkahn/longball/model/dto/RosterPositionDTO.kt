package net.alexanderkahn.longball.model.dto

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

data class RosterPositionDTO(val id: UUID?, val created: OffsetDateTime, val lastModified: OffsetDateTime, val team: UUID, val player: UUID, val jerseyNumber: Number, val startDate: LocalDate, val endDate: LocalDate? = null)