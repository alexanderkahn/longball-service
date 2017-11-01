package net.alexanderkahn.longball.model.dto

import java.time.OffsetDateTime
import java.util.*

data class PersonDTO(val id: UUID?, val created: OffsetDateTime, val lastModified: OffsetDateTime, val first: String, val last: String)