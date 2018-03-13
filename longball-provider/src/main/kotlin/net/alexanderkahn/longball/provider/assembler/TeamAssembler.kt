package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.api.exception.InvalidRelationshipsException
import net.alexanderkahn.longball.api.exception.ResourceNotFoundException
import net.alexanderkahn.longball.model.RequestTeam
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
        val league = team.relationships.league.data.let { leagueRepository.findByIdAndOwner(it.id, owner) ?: throw InvalidRelationshipsException(listOf(ResourceNotFoundException(it))) }
        return with(team.attributes) {TeamEntity(league, abbreviation, location, nickname, owner) }
    }
}