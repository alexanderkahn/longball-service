package net.alexanderkahn.longball.service

import net.alexanderkahn.longball.service.persistence.PlayerRepository
import net.alexanderkahn.longball.service.persistence.RosterPlayerRepository
import net.alexanderkahn.longball.service.persistence.TeamRepository
import net.alexanderkahn.longball.service.persistence.model.PersistencePlayer
import net.alexanderkahn.longball.service.persistence.model.PersistenceRosterPlayer
import net.alexanderkahn.longball.service.persistence.model.PersistenceTeam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class TestLoader(
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val playerRepository: PlayerRepository,
        @Autowired private val rosterPlayerRepository: RosterPlayerRepository
) {
    fun loadData() {
        deleteEverything()
        loadTeamWithPlayers("Away")
        loadTeamWithPlayers("Home")
    }

    private fun deleteEverything() {
        teamRepository.deleteAll()
        playerRepository.deleteAll()
        rosterPlayerRepository.deleteAll()
    }

    private fun loadTeamWithPlayers(location: String) {
        val team: PersistenceTeam = PersistenceTeam(abbreviation = location.toUpperCase(), location = location, nickname = "Team")
        teamRepository.save(team)
        val awayPlayers: List<PersistencePlayer> = (1..9).map { PersistencePlayer(first = location, last = it.toString()) }
        awayPlayers.forEach { player ->
            playerRepository.save(player)
            rosterPlayerRepository.save(PersistenceRosterPlayer(team = team, player = player, jerseyNumber = Random().nextInt(99).toShort(), startDate = OffsetDateTime.now()))
        }
    }
}