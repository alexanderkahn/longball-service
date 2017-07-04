package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.Player
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IPlayerService {

    fun getAll(pageable: Pageable): Page<Player>
    fun get(id: Long): Player

}