package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.League
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ILeagueService {

    fun get(id: UUID): League
    fun getAll(pageable: Pageable): Page<League>

}