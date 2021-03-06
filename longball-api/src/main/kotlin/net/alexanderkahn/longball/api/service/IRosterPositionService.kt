package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.model.RequestRosterPosition
import net.alexanderkahn.longball.model.ResponseRosterPosition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*
import javax.validation.Valid

interface IRosterPositionService {

    fun getAll(pageable: Pageable): Page<ResponseRosterPosition>
    fun get(id: UUID): ResponseRosterPosition
    fun save(@Valid rosterPosition: RequestRosterPosition): ResponseRosterPosition
    fun delete(id: UUID)
}