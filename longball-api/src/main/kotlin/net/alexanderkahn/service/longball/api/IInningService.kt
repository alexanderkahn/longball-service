package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.dto.InningDTO
import net.alexanderkahn.longball.model.dto.InningSideDTO
import net.alexanderkahn.longball.model.type.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IInningService {

    fun getInningsForGame(pageable: Pageable, gameId: UUID): Page<InningDTO>
    fun advanceInning(gameId: UUID)
    fun getInning(gameId: UUID, inningNumber: Int): InningDTO
    fun getInningSide(gameId: UUID, inningNumber: Int, inningSide: Side): InningSideDTO
}