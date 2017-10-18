package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import net.alexanderkahn.longball.provider.assembler.RosterPositionAssembler
import net.alexanderkahn.longball.provider.assembler.embeddableUser
import net.alexanderkahn.longball.provider.assembler.toDTO
import net.alexanderkahn.longball.provider.repository.RosterPositionRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.IRosterPositionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class RosterPositionService(
        @Autowired private val rosterPositionRepository: RosterPositionRepository,
        @Autowired private val rosterPositionAssembler: RosterPositionAssembler) : IRosterPositionService {

    override fun getAll(pageable: Pageable): Page<RosterPositionDTO> {
        val positions = rosterPositionRepository.findByOwner(pageable, UserContext.embeddableUser)
        return positions.map { it.toDTO() }
    }

    override fun get(id: UUID): RosterPositionDTO {
        val position = rosterPositionRepository.findByIdAndOwner(id, UserContext.embeddableUser)
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