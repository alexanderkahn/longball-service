package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.api.exception.InvalidRelationshipsException
import net.alexanderkahn.longball.api.exception.ResourceNotFoundException
import net.alexanderkahn.longball.api.model.RequestRosterPosition
import net.alexanderkahn.longball.provider.entity.BaseEntity
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class RosterPositionAssembler @Autowired constructor(
        private val userService: UserService,
        private val teamRepository: TeamRepository,
        private val personRepository: PersonRepository) {

    fun toEntity(rosterPosition: RequestRosterPosition): RosterPositionEntity {
        val owner = userService.userEntity()
        val team = with(rosterPosition.relationships.team.data) { EntityValidator(this, teamRepository.findByIdAndOwner(id, owner)) }
        val player = with(rosterPosition.relationships.player.data) { EntityValidator(this, personRepository.findByIdAndOwner(id, owner)) }
        if (team.entity == null || player.entity == null) {
            throw InvalidRelationshipsException(listOf(team, player).filter { it.entity == null }.map { ResourceNotFoundException(it.identifier) })
        }
        return with(rosterPosition.attributes) { RosterPositionEntity(team.entity, player.entity, jerseyNumber, startDate.toPersistence(), endDate?.toPersistence(), owner) }
    }

    data class EntityValidator<out T : BaseEntity>(val identifier: RelationshipObject.RelationshipObjectIdentifier, val entity: T?)
}