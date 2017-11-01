package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.RequestTeam
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.base.model.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TeamAssembler @Autowired constructor(
        private val userService: UserService,
        private val leagueRepository: LeagueRepository) {

    fun toEntity(team: RequestTeam): TeamEntity {
        val league = team.relationships.league.data.id.let { leagueRepository.findOne(it) ?: throw NotFoundException("leagues", it) }
        return with(team.attributes) {TeamEntity(league, abbreviation, location, nickname, userService.embeddableUser()) }
    }
}