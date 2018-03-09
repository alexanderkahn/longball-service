package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.exception.NotFoundException
import net.alexanderkahn.longball.api.model.RequestRosterPosition
import net.alexanderkahn.longball.api.model.ResponseRosterPosition
import net.alexanderkahn.longball.api.service.IRosterPositionService
import net.alexanderkahn.longball.provider.assembler.RosterPositionAssembler
import net.alexanderkahn.longball.provider.assembler.toResponse
import net.alexanderkahn.longball.provider.repository.RosterPositionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.Valid

@Service
@Validated
open class RosterPositionService @Autowired constructor(
        private val userService: UserService,
        private val rosterPositionRepository: RosterPositionRepository,
        private val rosterPositionAssembler: RosterPositionAssembler) : IRosterPositionService {

    override fun getAll(pageable: Pageable): Page<ResponseRosterPosition> {
        val positions = rosterPositionRepository.findByOwnerOrderByCreated(userService.userEntity(), pageable)
        return positions.map { it.toResponse() }
    }

    override fun get(id: UUID): ResponseRosterPosition {
        val position = rosterPositionRepository.findByIdAndOwner(id, userService.userEntity())
        return position?.toResponse() ?: throw NotFoundException("rosterpositions", id)
    }

    override fun save(@Valid rosterPosition: RequestRosterPosition): ResponseRosterPosition {
        val entity = rosterPositionAssembler.toEntity(rosterPosition)
        rosterPositionRepository.save(entity)
        return entity.toResponse()
    }

    override fun delete(id: UUID) {
        if (!rosterPositionRepository.existsById(id)) {
            throw NotFoundException("rosterpositions", id)
        }
        rosterPositionRepository.deleteById(id)
    }
}