package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*

@Service
class FakeGameService {

    private val fakeGame = Game(awayTeam = Team(1, "AWAY", "Away", "Team"),
            homeTeam = Team(2, "HOME", "Home", "Team"),
            startTime = ZonedDateTime.now())

    fun get(id: Long): Game {
        return fakeGame
    }

    fun getAll(): Page<Game> {
        return PageImpl<Game>(arrayListOf(fakeGame))
    }

    fun getStatus(gameId: Long): GameStatus {
        val appearance = PlateAppearance(Player(1, "Pitch", "Guy"), Player(2, "Bat", "man"), PlateAppearanceCount(3, 0))
        val basePath = BasePath(Base(Player(3, "First", "Base")), Base(null), Base(Player(4, "Third", "Base")))
        val innings = (1..4).map { Inning(Random().nextInt(5), Random().nextInt(5)) }
        return GameStatus(appearance, basePath, InningSummary(innings))
    }
}