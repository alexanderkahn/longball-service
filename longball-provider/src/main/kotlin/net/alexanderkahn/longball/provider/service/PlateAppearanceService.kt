package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.PlateAppearanceDTO
import net.alexanderkahn.longball.model.type.Side
import net.alexanderkahn.longball.provider.assembler.toDTO
import net.alexanderkahn.longball.provider.repository.PlateAppearanceRepository
import net.alexanderkahn.service.longball.api.IPlateAppearanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlateAppearanceService @Autowired constructor(
        private val userService: UserService,
        private val plateAppearanceRepository: PlateAppearanceRepository) : IPlateAppearanceService {

    override fun getPlateAppearances(pageable: Pageable, gameId: UUID, inningNumber: Int, side: Side): Page<PlateAppearanceDTO> {
        return plateAppearanceRepository.findByOwnerAndGameAndInningNumberAndSide(pageable, userService.embeddableUser(), gameId, inningNumber, side).map { it.toDTO() }
    }
}