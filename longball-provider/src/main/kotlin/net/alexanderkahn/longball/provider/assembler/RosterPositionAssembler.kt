package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.base.api.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RosterPositionAssembler @Autowired constructor(
        private val userService: UserService,
        private val teamRepository: TeamRepository,
        private val personRepository: PersonRepository) {

    fun toEntity(rosterPosition: RosterPositionDTO): RosterPositionEntity {
        val team = teamRepository.findOne(rosterPosition.team) ?: throw NotFoundException("teams", rosterPosition.team)
        val player = personRepository.findOne(rosterPosition.player) ?: throw NotFoundException("people", rosterPosition.player)
        return RosterPositionEntity(team, player, rosterPosition.jerseyNumber, rosterPosition.startDate, rosterPosition.endDate, userService.embeddableUser())
    }
}