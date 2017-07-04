package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.Inning
import net.alexanderkahn.longball.model.InningSide
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IInningService {

    fun getInningsForGame(pageable: Pageable, gameId: Long): Page<Inning>
    fun advanceInning(gameId: Long)
    fun getInning(gameId: Long, inningNumber: Int): Inning
    fun getInningSide(gameId: Long, inningNumber: Int, inningSide: Side): InningSide
}