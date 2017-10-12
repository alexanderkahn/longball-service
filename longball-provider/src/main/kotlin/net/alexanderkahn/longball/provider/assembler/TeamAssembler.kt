package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.Team
import net.alexanderkahn.longball.provider.entity.PxTeam
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TeamAssembler(@Autowired private val leagueRepository: LeagueRepository) {

    fun toPersistence(team: Team): PxTeam {
        val league = leagueRepository.findOne(team.league) ?: throw NotFoundException("leagues", team.league)
        return PxTeam(league, team.abbreviation, team.location, team.nickname)
    }
}