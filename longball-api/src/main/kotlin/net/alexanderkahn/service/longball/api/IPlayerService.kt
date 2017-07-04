package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.Player
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IPlayerService {

    fun getAll(pageable: Pageable): Page<Player>
    fun get(id: UUID): Player

}