package net.alexanderkahn.longball.model.dto

import java.time.OffsetDateTime
import java.util.*

data class LeagueDTO(val id: UUID?, val created: OffsetDateTime, val lastModified: OffsetDateTime, val name: String)