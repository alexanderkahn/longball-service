package net.alexanderkahn.longball.presentation.rest.controller


import net.alexanderkahn.longball.model.dto.GameDTO
import net.alexanderkahn.longball.model.dto.LineupPositionDTO
import net.alexanderkahn.longball.model.type.Side
import net.alexanderkahn.service.longball.api.IGameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class GameController(@Autowired private val gameService: IGameService) {

    @GetMapping("/games")
    fun getGames(pageable: Pageable): Page<GameDTO> {
        return gameService.getAll(pageable)
    }

    @GetMapping("/games/{id}")
    fun getGame(@PathVariable id: UUID): GameDTO {
        return gameService.getOne(id)
    }

    @GetMapping("/games/{id}/lineups/away")
    fun getAwayLineup(pageable: Pageable, @PathVariable id: UUID): Page<LineupPositionDTO> {
        return gameService.getLineupPlayers(pageable, id, Side.TOP)
    }

    @GetMapping("/games/{id}/lineups/home")
    fun getHomeLineup(pageable: Pageable, @PathVariable id: UUID): Page<LineupPositionDTO> {
        return gameService.getLineupPlayers(pageable, id, Side.BOTTOM)
    }


}