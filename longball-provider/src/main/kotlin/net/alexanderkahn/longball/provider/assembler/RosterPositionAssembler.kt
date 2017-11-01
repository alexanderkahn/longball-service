package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.RequestRosterPosition
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.base.model.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RosterPositionAssembler @Autowired constructor(
        private val userService: UserService,
        private val teamRepository: TeamRepository,
        private val personRepository: PersonRepository) {

    fun toEntity(rosterPosition: RequestRosterPosition): RosterPositionEntity {
        val team = rosterPosition.relationships.team.data.id.let { teamRepository.findOne(it) ?: throw NotFoundException("teams", it) }
        val player = rosterPosition.relationships.player.data.id.let { personRepository.findOne(it) ?: throw NotFoundException("people", it) }
        return with(rosterPosition.attributes) { RosterPositionEntity(team, player, jerseyNumber, startDate, endDate, userService.embeddableUser()) }
    }
}