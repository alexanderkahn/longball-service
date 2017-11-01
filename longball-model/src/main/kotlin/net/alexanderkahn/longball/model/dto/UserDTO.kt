package net.alexanderkahn.longball.model.dto

import java.time.OffsetDateTime
import java.util.*

data class UserDTO(val id: UUID, val created: OffsetDateTime, val lastModified: OffsetDateTime, val issuer: String, val username: String)