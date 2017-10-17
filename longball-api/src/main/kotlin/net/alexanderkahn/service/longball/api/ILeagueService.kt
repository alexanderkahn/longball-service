package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.dto.LeagueDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ILeagueService {

    fun get(id: UUID): LeagueDTO
    fun getAll(pageable: Pageable): Page<LeagueDTO>
    fun save(league: LeagueDTO): LeagueDTO
    fun delete(id: UUID)
}