package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.PlateAppearance
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.provider.assembler.pxUser
import net.alexanderkahn.longball.provider.assembler.toModel
import net.alexanderkahn.longball.provider.repository.PlateAppearanceRepository
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.IPlateAppearanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlateAppearanceService(
        @Autowired private val plateAppearanceRepository: PlateAppearanceRepository) : IPlateAppearanceService {

    override fun getPlateAppearances(pageable: Pageable, gameId: UUID, inningNumber: Int, side: Side): Page<PlateAppearance> {
        return plateAppearanceRepository.findByOwnerAndGameAndInningNumberAndSide(pageable, UserContext.pxUser, gameId, inningNumber, side).map { it.toModel() }
    }
}