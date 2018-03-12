package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.api.exception.InvalidRelationshipException
import net.alexanderkahn.longball.api.model.RequestTeam
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TeamAssembler @Autowired constructor(
        private val userService: UserService,
        private val leagueRepository: LeagueRepository) {

    fun toEntity(team: RequestTeam): TeamEntity {
        val owner = userService.userEntity()
        val league = team.relationships.league.data.let { leagueRepository.findByIdAndOwner(it.id, owner) ?: throw InvalidRelationshipException(it) }
        return with(team.attributes) {TeamEntity(league, abbreviation, location, nickname, owner) }
    }
}