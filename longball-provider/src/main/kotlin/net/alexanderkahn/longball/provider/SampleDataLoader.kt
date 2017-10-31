package net.alexanderkahn.longball.provider


import javafx.geometry.Side
import net.alexanderkahn.longball.model.type.FieldPosition
import net.alexanderkahn.longball.provider.entity.*
import net.alexanderkahn.longball.provider.repository.*
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.longball.api.ISampleDataLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

@Service
class SampleDataLoader(
        @Autowired private val userService: UserService,
        @Autowired private val leagueRepository: LeagueRepository,
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val personRepository: PersonRepository,
        @Autowired private val rosterPositionRepository: RosterPositionRepository,
        @Autowired private val gameRepository: GameRepository,
        @Autowired private val lineupPlayerRepository: LineupPlayerRepository
) : ISampleDataLoader {

    override fun loadSampleData() {
        val league = loadLeague()
        val awayTeam = loadTeamWithPlayers(league, "Away")
        val homeTeam = loadTeamWithPlayers(league, "Home")
        createGame(league, awayTeam, homeTeam)
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

    private fun createGame(league: LeagueEntity, awayTeam: TeamEntity, homeTeam: TeamEntity) {
        val game = GameEntity(league, awayTeam, homeTeam, OffsetDateTime.now(), null, userService.embeddableUser())
        gameRepository.save(game)
        createLineup(game, awayTeam, Side.TOP)
        createLineup(game, homeTeam, Side.BOTTOM)
    }

    private fun createLineup(game: GameEntity, team: TeamEntity, side: Side) {
        val rosterPlayers = rosterPositionRepository.findByTeamIdAndOwner(org.springframework.data.domain.PageRequest(0, 20), team.id, userService.embeddableUser())
        var counter = 0
        FieldPosition.values().forEach { it ->
            val lPosition = LineupPositionEntity(game, rosterPlayers.content[counter].player, side.ordinal, ++counter, it.positionNotation, userService.embeddableUser())
            lineupPlayerRepository.save(lPosition)
        }
    }
}