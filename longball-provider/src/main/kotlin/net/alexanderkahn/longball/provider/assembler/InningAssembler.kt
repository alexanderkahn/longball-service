package net.alexanderkahn.longball.provider.assembler


import net.alexanderkahn.longball.model.dto.InningDTO
import net.alexanderkahn.longball.model.dto.InningSideDTO
import net.alexanderkahn.longball.model.type.PlateAppearanceResultType
import net.alexanderkahn.longball.model.type.Side
import net.alexanderkahn.longball.provider.entity.InningEntity
import net.alexanderkahn.longball.provider.entity.InningSideEntity
import net.alexanderkahn.longball.provider.repository.InningSideRepository
import net.alexanderkahn.longball.provider.repository.PlateAppearanceRepository
import net.alexanderkahn.service.base.api.security.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class InningAssembler(
        @Autowired private val inningSideRepository: InningSideRepository,
        @Autowired private val plateAppearanceRepository: PlateAppearanceRepository) {

    fun toModel(entity: InningEntity): InningDTO {
        return InningDTO(entity.inningNumber, getInningSide(entity, Side.TOP) ?: throw Exception(), getInningSide(entity, Side.BOTTOM))
    }

    private fun getInningSide(inning: InningEntity, side: Side): InningSideDTO? {
        return inningSideRepository.findByInningAndSideAndOwner(inning, side, UserContext.pxUser)?.toModel()
    }

    fun InningSideEntity.toModel(): InningSideDTO {
        val appearances = plateAppearanceRepository.findBySideAndOwner(this, UserContext.pxUser)
        val hits = appearances.count { it.plateAppearanceResult in listOf(PlateAppearanceResultType.IN_PLAY) }
        //FIXME These values can be calculated from PlateAppearanceResult. Come finish this when PlateAppearances look right
        return InningSideDTO(0, 0, 0, 0, 0)
    }

}