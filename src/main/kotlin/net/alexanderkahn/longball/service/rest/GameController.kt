package net.alexanderkahn.longball.service.rest

import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

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
    fun getAwayLineup(pageable: Pageable, @PathVariable id: Long): Page<LineupPlayer> {
        return gameService.getLineupPlayers(pageable, id, InningSide.TOP)
    }

    @GetMapping("/{id}/lineups/home")
    fun getHomeLineup(pageable: Pageable, @PathVariable id: Long): Page<LineupPlayer> {
        return gameService.getLineupPlayers(pageable, id, InningSide.BOTTOM)
    }

    @GetMapping("/{id}/plateappearance")
    fun getGameStatus(@PathVariable id: Long): PlateAppearance {
        return gameService.getCurrentPlateAppearance(id)
    }

    @PostMapping("/{id}/plateappearance/event")
    fun addGameplayEvent(@PathVariable id: Long, @RequestBody gameplayEvent: GameplayEvent): PlateAppearance {
        return gameService.addGameplayEvent(id, gameplayEvent)
    }
}