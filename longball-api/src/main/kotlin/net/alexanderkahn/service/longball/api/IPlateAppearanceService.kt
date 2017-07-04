package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.PlateAppearance
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IPlateAppearanceService {

    fun getPlateAppearances(pageable: Pageable, gameId: UUID, inningNumber: Int, side: Side): Page<PlateAppearance>

}