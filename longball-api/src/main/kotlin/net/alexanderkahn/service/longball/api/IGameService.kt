package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.GameDTO
import net.alexanderkahn.longball.model.LineupPositionDTO
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IGameService {
    fun getOne(id: UUID): GameDTO
    fun getAll(pageable: Pageable): Page<GameDTO>
    fun getLineupPlayers(pageable: Pageable, gameId: UUID, side: Side): Page<LineupPositionDTO>
}