package net.alexanderkahn.longball.service.model

import org.springframework.data.annotation.Id

data class Player(val first: String, val last: String) {
    @Id var id: String? = null
}