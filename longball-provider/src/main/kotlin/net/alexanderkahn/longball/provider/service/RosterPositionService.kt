package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import net.alexanderkahn.longball.provider.assembler.RosterPositionAssembler
import net.alexanderkahn.longball.provider.assembler.toDTO
import net.alexanderkahn.longball.provider.repository.RosterPositionRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.longball.api.IRosterPositionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class RosterPositionService @Autowired constructor(
        private val userService: UserService,
        private val rosterPositionRepository: RosterPositionRepository,
        private val rosterPositionAssembler: RosterPositionAssembler) : IRosterPositionService {

    override fun getAll(pageable: Pageable): Page<RosterPositionDTO> {
        val positions = rosterPositionRepository.findByOwner(pageable, userService.embeddableUser())
        return positions.map { it.toDTO() }
    }

    override fun get(id: UUID): RosterPositionDTO {
        val position = rosterPositionRepository.findByIdAndOwner(id, userService.embeddableUser())
        return position?.toDTO() ?: throw NotFoundException("rosterpositions", id)
    }

    override fun save(rosterPosition: RosterPositionDTO): RosterPositionDTO {
        val entity = rosterPositionAssembler.toEntity(rosterPosition)
        rosterPositionRepository.save(entity)
        return entity.toDTO()
    }

    override fun delete(id: UUID) {
        if (!rosterPositionRepository.exists(id)) {
            throw NotFoundException("rosterpositions", id)
        }
        rosterPositionRepository.delete(id)
    }
}