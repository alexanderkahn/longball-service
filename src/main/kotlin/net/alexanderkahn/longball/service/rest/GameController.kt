package net.alexanderkahn.longball.service.rest

import net.alexanderkahn.longball.service.model.Game
import net.alexanderkahn.longball.service.model.GameStatus
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.model.LineupPosition
import net.alexanderkahn.longball.service.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController(@Autowired private val gameService: GameService) {

    @GetMapping
    fun getGames(pageable: Pageable): Page<Game> {
        return gameService.getAll(pageable)
    }

    @GetMapping("/{id}")
    fun getGame(@PathVariable id: Long): Game {
        return gameService.get(id)
    }

    @GetMapping("/{id}/lineups/away")
    fun getAwayLineup(pageable: Pageable, @PathVariable id: Long): Page<LineupPosition> {
        return gameService.getLineupPositions(pageable, id, InningHalf.TOP)
    }

    @GetMapping("/{id}/lineups/home")
    fun getHomeLineup(pageable: Pageable, @PathVariable id: Long): Page<LineupPosition> {
        return gameService.getLineupPositions(pageable, id, InningHalf.BOTTOM)
    }

    @GetMapping("/{id}/status")
    fun getGameStatus(@PathVariable id: Long): GameStatus {
        return gameService.getFakeStatus(id)
    }
}