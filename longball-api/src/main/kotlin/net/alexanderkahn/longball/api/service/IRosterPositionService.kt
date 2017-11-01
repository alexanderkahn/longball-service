package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.model.dto.RequestRosterPosition
import net.alexanderkahn.longball.model.dto.ResponseRosterPosition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IRosterPositionService {

    fun getAll(pageable: Pageable): Page<ResponseRosterPosition>
    fun get(id: UUID): ResponseRosterPosition
    fun save(rosterPosition: RequestRosterPosition): ResponseRosterPosition
    fun delete(id: UUID)
}