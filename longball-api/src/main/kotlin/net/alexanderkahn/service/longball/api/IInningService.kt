package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.Inning
import net.alexanderkahn.longball.model.InningSide
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IInningService {

    fun getInningsForGame(pageable: Pageable, gameId: UUID): Page<Inning>
    fun advanceInning(gameId: UUID)
    fun getInning(gameId: UUID, inningNumber: Int): Inning
    fun getInningSide(gameId: UUID, inningNumber: Int, inningSide: Side): InningSide
}