package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.PlateAppearance
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IPlateAppearanceService {

    fun getPlateAppearances(pageable: Pageable, gameId: Long, inningNumber: Int, side: Side): Page<PlateAppearance>

}