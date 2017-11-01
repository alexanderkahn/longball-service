package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.service.ISampleDataLoader
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.PersonEntity
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.longball.provider.repository.RosterPositionRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class SampleDataLoader(
        @Autowired private val userService: UserService,
        @Autowired private val leagueRepository: LeagueRepository,
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val personRepository: PersonRepository,
        @Autowired private val rosterPositionRepository: RosterPositionRepository
) : ISampleDataLoader {

    override fun loadSampleData() {
        val league = loadLeague()
        loadTeamWithPlayers(league, "Away")
        loadTeamWithPlayers(league, "Home")
    }

    private fun loadLeague(): LeagueEntity {
        val league = LeagueEntity("Example League", userService.embeddableUser())
        leagueRepository.save(league)
        return league
    }

    private fun loadTeamWithPlayers(league: LeagueEntity, location: String): TeamEntity {
        val team = TeamEntity(league, location.toUpperCase(), location, "Team", userService.embeddableUser())
        teamRepository.save(team)
        val awayPlayers: List<PersonEntity> = (1..9).map { PersonEntity(location, it.toString(), userService.embeddableUser()) }
        awayPlayers.forEach { player ->
            personRepository.save(player)
            rosterPositionRepository.save(RosterPositionEntity(team, player, Random().nextInt(99), LocalDate.now(), null, userService.embeddableUser()))
        }
        return team
    }
}