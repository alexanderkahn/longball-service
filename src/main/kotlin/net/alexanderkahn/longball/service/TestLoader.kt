package net.alexanderkahn.longball.service

import net.alexanderkahn.base.servicebase.security.jwt.jwtTestUser
import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.assembler.toPersistence
import net.alexanderkahn.longball.service.persistence.model.*
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
        @Autowired private val lineupPositionRepository: LineupPositionRepository
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

    private fun loadLeague(): PersistenceLeague {
        val league = PersistenceLeague(null, owner, "Example League")
        leagueRepository.save(league)
        return league
    }

    private fun loadTeamWithPlayers(location: String): PersistenceTeam {
        val team: PersistenceTeam = PersistenceTeam(owner = owner, abbreviation = location.toUpperCase(), location = location, nickname = "Team")
        teamRepository.save(team)
        val awayPlayers: List<PersistencePlayer> = (1..9).map { PersistencePlayer(owner = owner, first = location, last = it.toString()) }
        awayPlayers.forEach { player ->
            playerRepository.save(player)
            rosterPlayerRepository.save(PersistenceRosterPlayer(owner = owner, team = team, player = player, jerseyNumber = Random().nextInt(99).toShort(), startDate = OffsetDateTime.now()))
        }
        return team
    }

    private fun createGame(league: PersistenceLeague, awayTeam: PersistenceTeam, homeTeam: PersistenceTeam) {
        val game: PersistenceGame = PersistenceGame(null, owner, league, awayTeam, homeTeam, OffsetDateTime.now())
        gameRepository.save(game)
        createLineup(game, awayTeam, InningHalf.TOP)
        createLineup(game, homeTeam, InningHalf.BOTTOM)
    }

    private fun createLineup(game: PersistenceGame, team: PersistenceTeam, inningHalf: InningHalf) {
        val rosterPlayers = rosterPlayerRepository.findByOwnerAndTeamId(UserContext.getPersistenceUser(), team.id!!, PageRequest(0, 20))
        var counter = 0
        FieldPosition.values().forEach { it ->
            val lPosition = PersistenceLineupPosition(null, owner, game, rosterPlayers.content[counter].player, inningHalf, (++counter).toShort(), it)
            lineupPositionRepository.save(lPosition)
        }
    }
}