package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.api.exception.InvalidRelationshipException
import net.alexanderkahn.longball.api.model.RequestRosterPosition
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.longball.provider.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RosterPositionAssembler @Autowired constructor(
        private val userService: UserService,
        private val teamRepository: TeamRepository,
        private val personRepository: PersonRepository) {

    fun toEntity(rosterPosition: RequestRosterPosition): RosterPositionEntity {
        val owner = userService.userEntity()
        val team = rosterPosition.relationships.team.data.let { teamRepository.findByIdAndOwner(it.id, owner) ?: throw InvalidRelationshipException(it) }
        val player = rosterPosition.relationships.player.data.let { personRepository.findByIdAndOwner(it.id, owner) ?: throw InvalidRelationshipException(it) }
        return with(rosterPosition.attributes) { RosterPositionEntity(team, player, jerseyNumber, startDate.toPersistence(), endDate?.toPersistence(), owner) }
    }


}