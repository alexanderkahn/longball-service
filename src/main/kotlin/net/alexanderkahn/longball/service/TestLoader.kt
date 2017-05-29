package net.alexanderkahn.longball.service

import net.alexanderkahn.servicebase.provider.security.jwt.jwtTestUser
import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.Side
import net.alexanderkahn.longball.service.persistence.entity.*
import net.alexanderkahn.longball.service.persistence.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class TestLoader(
        @Autowired private val leagueRepository: LeagueRepository,
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val playerRepository: PlayerRepository,
        @Autowired private val rosterPlayerRepository: RosterPlayerRepository,
        @Autowired private val gameRepository: GameRepository,
        @Autowired private val lineupPlayerRepository: LineupPlayerRepository
) { init {
    UserContext.setCurrentUser(jwtTestUser())
}

    fun loadData() {
        val league = loadLeague()
        val awayTeam = loadTeamWithPlayers("Away")
        val homeTeam = loadTeamWithPlayers("Home")
        createGame(league, awayTeam, homeTeam)
    }

    private fun loadLeague(): PxLeague {
        val league = PxLeague("Example League")
        leagueRepository.save(league)
        return league
    }

    private fun loadTeamWithPlayers(location: String): PxTeam {
        val team: PxTeam = PxTeam(abbreviation = location.toUpperCase(), location = location, nickname = "Team")
        teamRepository.save(team)
        val awayPlayers: List<PxPlayer> = (1..9).map { PxPlayer(first = location, last = it.toString()) }
        awayPlayers.forEach { player ->
            playerRepository.save(player)
            rosterPlayerRepository.save(PxRosterPlayer(team = team, player = player, jerseyNumber = Random().nextInt(99), startDate = OffsetDateTime.now()))
        }
        return team
    }

    private fun createGame(league: PxLeague, awayTeam: PxTeam, homeTeam: PxTeam) {
        val game: PxGame = PxGame(league, awayTeam, homeTeam, OffsetDateTime.now())
        gameRepository.save(game)
        createLineup(game, awayTeam, Side.TOP)
        createLineup(game, homeTeam, Side.BOTTOM)
    }

    private fun createLineup(game: PxGame, team: PxTeam, side: Side) {
        val rosterPlayers = rosterPlayerRepository.findByTeamIdAndOwner(PageRequest(0, 20), team.id!!, UserContext.getPersistenceUser())
        var counter = 0
        FieldPosition.values().forEach { it ->
            val lPosition = PxLineupPlayer(game, rosterPlayers.content[counter].player, side, ++counter, it)
            lineupPlayerRepository.save(lPosition)
        }
    }
}