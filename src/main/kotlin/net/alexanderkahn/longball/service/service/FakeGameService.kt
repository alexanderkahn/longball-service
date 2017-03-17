package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.Game
import net.alexanderkahn.longball.service.model.Team
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class FakeGameService {

    private val fakeGame = Game(awayTeam = Team("AWAY", "Away", "Team"),
            homeTeam = Team("HOME", "Home", "Team"),
            startTime = ZonedDateTime.now())

    fun get(id: String): Game {
        return fakeGame
    }
}