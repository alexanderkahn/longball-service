package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.dto.PlateAppearanceDTO
import net.alexanderkahn.longball.model.type.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IPlateAppearanceService {

    fun getPlateAppearances(pageable: Pageable, gameId: UUID, inningNumber: Int, side: Side): Page<PlateAppearanceDTO>

}