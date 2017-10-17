package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.model.PlateAppearanceDTO
import net.alexanderkahn.longball.presentation.getSideFromParam
import net.alexanderkahn.service.longball.api.IPlateAppearanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class PlateAppearanceController(@Autowired private val plateAppearanceService: IPlateAppearanceService) {

    @GetMapping("/games/{gameId}/innings/{inningNumber}/{side:top|bottom}/plateAppearances")
    fun getInningSide(pageable: Pageable, @PathVariable gameId: UUID, @PathVariable inningNumber: Int, @PathVariable side: String): Page<PlateAppearanceDTO> {
        return plateAppearanceService.getPlateAppearances(pageable, gameId, inningNumber, getSideFromParam(side))
    }

}
