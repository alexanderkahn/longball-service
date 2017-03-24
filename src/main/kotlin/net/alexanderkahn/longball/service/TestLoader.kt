package net.alexanderkahn.longball.service

import net.alexanderkahn.longball.service.persistence.model.PersistenceGame
import net.alexanderkahn.longball.service.persistence.model.PersistencePlayer
import net.alexanderkahn.longball.service.persistence.model.PersistenceRosterPlayer
import net.alexanderkahn.longball.service.persistence.model.PersistenceTeam
import net.alexanderkahn.longball.service.persistence.repository.GameRepository
import net.alexanderkahn.longball.service.persistence.repository.PlayerRepository
import net.alexanderkahn.longball.service.persistence.repository.RosterPlayerRepository
import net.alexanderkahn.longball.service.persistence.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class TestLoader(
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val playerRepository: PlayerRepository,
        @Autowired private val rosterPlayerRepository: RosterPlayerRepository,
        @Autowired private val gameRepository: GameRepository
) {
    fun loadData() {
        deleteEverything()
        val awayTeam = loadTeamWithPlayers("Away")
        val homeTeam = loadTeamWithPlayers("Home")
        createGame(awayTeam, homeTeam)
    }

    private fun deleteEverything() {
        teamRepository.deleteAll()
        playerRepository.deleteAll()
        rosterPlayerRepository.deleteAll()
        gameRepository.deleteAll()
    }

    private fun loadTeamWithPlayers(location: String): PersistenceTeam {
        val team: PersistenceTeam = PersistenceTeam(abbreviation = location.toUpperCase(), location = location, nickname = "Team")
        teamRepository.save(team)
        val awayPlayers: List<PersistencePlayer> = (1..9).map { PersistencePlayer(first = location, last = it.toString()) }
        awayPlayers.forEach { player ->
            playerRepository.save(player)
            rosterPlayerRepository.save(PersistenceRosterPlayer(team = team, player = player, jerseyNumber = Random().nextInt(99).toShort(), startDate = OffsetDateTime.now()))
        }
        return team
    }

    private fun createGame(awayTeam: PersistenceTeam, homeTeam: PersistenceTeam) {
        val game: PersistenceGame = PersistenceGame(null, awayTeam, homeTeam, OffsetDateTime.now())
        gameRepository.save(game)
    }
}