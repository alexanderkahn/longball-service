package net.alexanderkahn.longball.core

import net.alexanderkahn.longball.core.assembler.pxUser
import net.alexanderkahn.longball.model.FieldPosition
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.core.persistence.model.*
import net.alexanderkahn.longball.core.persistence.repository.*
import net.alexanderkahn.servicebase.core.security.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class TestLoaderService (
        @Autowired private val leagueRepository: LeagueRepository,
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val playerRepository: PlayerRepository,
        @Autowired private val rosterPlayerRepository: RosterPlayerRepository,
        @Autowired private val gameRepository: GameRepository,
        @Autowired private val lineupPlayerRepository: LineupPlayerRepository
) {

    fun loadData() {
        val league = loadLeague()
        val awayTeam = loadTeamWithPlayers("Away")
        val homeTeam = loadTeamWithPlayers("Home")
        createGame(league, awayTeam, homeTeam)
    }

    private fun loadLeague(): PxLeague {
        val league = PxLeague(UserContext.pxUser, "Example League")
        leagueRepository.save(league)
        return league
    }

    private fun loadTeamWithPlayers(location: String): PxTeam {
        val team: PxTeam = PxTeam(UserContext.pxUser, location.toUpperCase(), location, "Team")
        teamRepository.save(team)
        val awayPlayers: List<PxPlayer> = (1..9).map { PxPlayer(owner = UserContext.pxUser, first = location, last = it.toString()) }
        awayPlayers.forEach { player ->
            playerRepository.save(player)
            rosterPlayerRepository.save(PxRosterPlayer(owner = UserContext.pxUser, team = team, player = player, jerseyNumber = Random().nextInt(99), startDate = OffsetDateTime.now()))
        }
        return team
    }

    private fun createGame(league: PxLeague, awayTeam: PxTeam, homeTeam: PxTeam) {
        val game: PxGame = PxGame(UserContext.pxUser, league, awayTeam, homeTeam, OffsetDateTime.now())
        gameRepository.save(game)
        createLineup(game, awayTeam, Side.TOP)
        createLineup(game, homeTeam, Side.BOTTOM)
    }

    private fun createLineup(game: PxGame, team: PxTeam, side: Side) {
        val rosterPlayers = rosterPlayerRepository.findByTeamIdAndOwner(PageRequest(0, 20), team.id!!, UserContext.pxUser)
        var counter = 0
        FieldPosition.values().forEach { it ->
            val lPosition = PxLineupPlayer(UserContext.pxUser, game, rosterPlayers.content[counter].player, side.ordinal, ++counter, it.positionNotation)
            lineupPlayerRepository.save(lPosition)
        }
    }
}