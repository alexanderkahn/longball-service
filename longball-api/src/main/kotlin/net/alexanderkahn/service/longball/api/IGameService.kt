package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.Game
import net.alexanderkahn.longball.model.LineupPlayer
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IGameService {
    fun getOne(id: UUID): Game
    fun getAll(pageable: Pageable): Page<Game>
    fun getLineupPlayers(pageable: Pageable, gameId: UUID, side: Side): Page<LineupPlayer>
}