package net.alexanderkahn.longball.presentation.rest


import net.alexanderkahn.longball.model.Game
import net.alexanderkahn.longball.model.LineupPlayer
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.core.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(@Autowired private val gameService: GameService) {

    @GetMapping("/games")
    fun getGames(pageable: Pageable): Page<Game> {
        return gameService.getAll(pageable)
    }

    @GetMapping("/games/{id}")
    fun getGame(@PathVariable id: Long): Game {
        return gameService.getOne(id)
    }

    @GetMapping("/games/{id}/lineups/away")
    fun getAwayLineup(pageable: Pageable, @PathVariable id: Long): Page<LineupPlayer> {
        return gameService.getLineupPlayers(pageable, id, Side.TOP)
    }

    @GetMapping("/games/{id}/lineups/home")
    fun getHomeLineup(pageable: Pageable, @PathVariable id: Long): Page<LineupPlayer> {
        return gameService.getLineupPlayers(pageable, id, Side.BOTTOM)
    }


}