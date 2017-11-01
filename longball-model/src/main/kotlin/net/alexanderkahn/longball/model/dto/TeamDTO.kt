package net.alexanderkahn.longball.model.dto

import java.time.OffsetDateTime
import java.util.*

data class TeamDTO(val id: UUID?, val created: OffsetDateTime, val lastModified: OffsetDateTime, val league: UUID, val abbreviation: String, val location: String, val nickname: String)