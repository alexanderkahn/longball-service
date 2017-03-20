package net.alexanderkahn.longball.service.model

import org.springframework.data.annotation.Id

data class Team(val abbreviation: String, val location: String, val nickname: String) {
    @Id var id: String? = null
}