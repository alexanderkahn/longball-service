package net.alexanderkahn.longball.service

import net.alexanderkahn.base.servicebase.security.jwt.jwtTestUser
import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.service.assembler.toPersistence
import net.alexanderkahn.longball.service.persistence.model.entity.*
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

    private val owner = UserContext.getCurrentUser().toPersistence()

    fun loadData() {
        val league = loadLeague()
        val awayTeam = loadTeamWithPlayers("Away")
        val homeTeam = loadTeamWithPlayers("Home")
        createGame(league, awayTeam, homeTeam)
    }

    private fun loadLeague(): PxLeague {
        val league = PxLeague(null, owner, "Example League")
        leagueRepository.save(league)
        return league
    }

    private fun loadTeamWithPlayers(location: String): PxTeam {
        val team: PxTeam = PxTeam(owner = owner, abbreviation = location.toUpperCase(), location = location, nickname = "Team")
        teamRepository.save(team)
        val awayPlayers: List<PxPlayer> = (1..9).map { PxPlayer(owner = owner, first = location, last = it.toString()) }
        awayPlayers.forEach { player ->
            playerRepository.save(player)
            rosterPlayerRepository.save(PxRosterPlayer(owner = owner, team = team, player = player, jerseyNumber = Random().nextInt(99).toShort(), startDate = OffsetDateTime.now()))
        }
        return team
    }

    private fun createGame(league: PxLeague, awayTeam: PxTeam, homeTeam: PxTeam) {
        val game: PxGame = PxGame(null, owner, league, awayTeam, homeTeam, OffsetDateTime.now())
        gameRepository.save(game)
        createLineup(game, awayTeam, InningHalf.TOP)
        createLineup(game, homeTeam, InningHalf.BOTTOM)
    }

    private fun createLineup(game: PxGame, team: PxTeam, inningHalf: InningHalf) {
        val rosterPlayers = rosterPlayerRepository.findByTeamIdAndOwner(PageRequest(0, 20), team.id!!, UserContext.getPersistenceUser())
        var counter = 0
        FieldPosition.values().forEach { it ->
            val lPosition = PxLineupPlayer(null, owner, game, rosterPlayers.content[counter].player, inningHalf, (++counter).toShort(), it)
            lineupPlayerRepository.save(lPosition)
        }
    }
}