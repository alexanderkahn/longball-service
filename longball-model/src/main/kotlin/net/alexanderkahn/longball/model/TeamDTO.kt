package net.alexanderkahn.longball.model

import java.util.*

data class TeamDTO(val id: UUID?, val league: UUID, val abbreviation: String, val location: String, val nickname: String)