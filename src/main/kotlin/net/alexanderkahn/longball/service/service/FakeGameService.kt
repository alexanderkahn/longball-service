package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*

@Service
class FakeGameService {

    private val fakeGame = Game(awayTeam = Team("AWAY", "Away", "Team"),
            homeTeam = Team("HOME", "Home", "Team"),
            startTime = ZonedDateTime.now())

    fun get(id: String): Game {
        return fakeGame
    }

    fun getAll(): Page<Game> {
        return PageImpl<Game>(arrayListOf(fakeGame))
    }

    fun getStatus(gameId: String): GameStatus {
        val appearance = PlateAppearance(Player("Pitch", "Guy"), Player("Bat", "man"), PlateAppearanceCount(3, 0))
        val basePath = BasePath(Base(Player("First", "Base")), Base(null), Base(Player("Third", "Base")))
        val innings = (1..4).map { Inning(Random().nextInt(5), Random().nextInt(5)) }
        return GameStatus(appearance, basePath, InningSummary(innings))
    }
}