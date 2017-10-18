package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IRosterPositionService {

    fun getAll(pageable: Pageable): Page<RosterPositionDTO>
    fun get(id: UUID): RosterPositionDTO
    fun save(rosterPosition: RosterPositionDTO): RosterPositionDTO
    fun delete(id: UUID)
}