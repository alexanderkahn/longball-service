package net.alexanderkahn.longball.service

import net.alexanderkahn.longball.service.model.Player
import net.alexanderkahn.longball.service.persistence.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestLoader(@Autowired private val playerRepository: PlayerRepository) {
    fun loadData() {
        loadPlayers()
    }

    private fun loadPlayers() {
        playerRepository.deleteAll()
        val awayPlayers: List<Player> = (1..9).map { Player("Away", it.toString()) }
        val homePlayers: List<Player> = (1..9).map { Player("Home", it.toString()) }

        awayPlayers.forEach { playerRepository.save(it) }
        homePlayers.forEach { playerRepository.save(it) }
    }
}