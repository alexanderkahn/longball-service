package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.TeamDTO
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.base.api.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TeamAssembler @Autowired constructor(
        private val userService: UserService,
        private val leagueRepository: LeagueRepository) {

    fun toEntity(team: TeamDTO): TeamEntity {
        val league = leagueRepository.findOne(team.league) ?: throw NotFoundException("leagues", team.league)
        return TeamEntity(league, team.abbreviation, team.location, team.nickname, userService.embeddableUser())
    }
}