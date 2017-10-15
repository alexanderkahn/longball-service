package net.alexanderkahn.longball.provider


import javafx.geometry.Side
import net.alexanderkahn.longball.model.FieldPosition
import net.alexanderkahn.longball.provider.entity.*
import net.alexanderkahn.longball.provider.repository.*
import net.alexanderkahn.service.longball.api.ISampleDataLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class SampleDataLoader(
        @Autowired private val leagueRepository: LeagueRepository,
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val personRepository: PersonRepository,
        @Autowired private val playerRepository: PlayerRepository,
        @Autowired private val gameRepository: GameRepository,
        @Autowired private val lineupPlayerRepository: LineupPlayerRepository
) : ISampleDataLoader {

    override fun loadSampleData() {
        val league = loadLeague()
        val awayTeam = loadTeamWithPlayers(league, "Away")
        val homeTeam = loadTeamWithPlayers(league, "Home")
        createGame(league, awayTeam, homeTeam)
    }

    private fun loadLeague(): PxLeague {
        val league = PxLeague("Example League")
        leagueRepository.save(league)
        return league
    }

    private fun loadTeamWithPlayers(league: PxLeague, location: String): PxTeam {
        val team = PxTeam(league, location.toUpperCase(), location, "Team")
        teamRepository.save(team)
        val awayPlayers: List<PxPerson> = (1..9).map { PxPerson(first = location, last = it.toString()) }
        awayPlayers.forEach { player ->
            personRepository.save(player)
            playerRepository.save(PxPlayer(team = team, player = player, jerseyNumber = Random().nextInt(99), startDate = OffsetDateTime.now()))
        }
        return team
    }

    private fun createGame(league: PxLeague, awayTeam: PxTeam, homeTeam: PxTeam) {
        val game = PxGame(league, awayTeam, homeTeam, OffsetDateTime.now())
        gameRepository.save(game)
        createLineup(game, awayTeam, Side.TOP)
        createLineup(game, homeTeam, Side.BOTTOM)
    }

    private fun createLineup(game: PxGame, team: PxTeam, side: Side) {
        val rosterPlayers = playerRepository.findByTeamIdAndOwner(org.springframework.data.domain.PageRequest(0, 20), team.id)
        var counter = 0
        FieldPosition.values().forEach { it ->
            val lPosition = PxLineupPlayer(game, rosterPlayers.content[counter].player, side.ordinal, ++counter, it.positionNotation)
            lineupPlayerRepository.save(lPosition)
        }
    }
}