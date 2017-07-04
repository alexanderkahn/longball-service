package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.League
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ILeagueService {

    fun get(id: Long): League
    fun getAll(pageable: Pageable): Page<League>

}