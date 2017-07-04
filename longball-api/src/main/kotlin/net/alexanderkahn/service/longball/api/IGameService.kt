package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.Game
import net.alexanderkahn.longball.model.LineupPlayer
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IGameService {
    fun getOne(id: Long): Game
    fun getAll(pageable: Pageable): Page<Game>
    fun getLineupPlayers(pageable: Pageable, gameId: Long, side: Side): Page<LineupPlayer>
}