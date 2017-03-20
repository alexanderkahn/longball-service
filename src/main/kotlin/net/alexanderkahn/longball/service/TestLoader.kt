package net.alexanderkahn.longball.service

import net.alexanderkahn.longball.service.model.Player
import net.alexanderkahn.longball.service.model.Team
import net.alexanderkahn.longball.service.persistence.PlayerRepository
import net.alexanderkahn.longball.service.persistence.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestLoader(
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val playerRepository: PlayerRepository
) {
    fun loadData() {
        teamRepository.deleteAll()
        playerRepository.deleteAll()
        loadTeamWithPlayers("Away")
        loadTeamWithPlayers("Home")
    }

    private fun loadTeamWithPlayers(location: String) {
        val awayTeam: Team = Team(location.toUpperCase(), location, "Team")
        teamRepository.save(awayTeam)
        val awayPlayers: List<Player> = (1..9).map { Player(location, it.toString()) }
        awayPlayers.forEach { playerRepository.save(it) }
    }
}