package net.alexanderkahn.longball.service.service.assembler

import net.alexanderkahn.longball.service.model.Inning
import net.alexanderkahn.longball.service.model.InningSide
import net.alexanderkahn.longball.service.model.PlateAppearanceResult
import net.alexanderkahn.longball.service.model.Side
import net.alexanderkahn.longball.service.persistence.entity.PxInning
import net.alexanderkahn.longball.service.persistence.entity.PxInningSide
import net.alexanderkahn.longball.service.persistence.repository.InningSideRepository
import net.alexanderkahn.longball.service.persistence.repository.PlateAppearanceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class InningAssembler(
        @Autowired private val inningSideRepository: InningSideRepository,
        @Autowired private val plateAppearanceRepository: PlateAppearanceRepository) {

    fun toModel(entity: PxInning): Inning {
        if (entity.id == null) {
            throw UnsupportedOperationException("Cannot convert unsaved inning")
        }
        return Inning(entity.inningNumber, getInningSide(entity, Side.TOP) ?: throw Exception(),  getInningSide(entity, Side.BOTTOM))
    }

    private fun getInningSide(inning: PxInning, side: Side): InningSide? {
        return inningSideRepository.findByInningAndSideAndOwner(inning, side)?.toModel()
    }

    fun PxInningSide.toModel(): InningSide {
        val appearances = plateAppearanceRepository.findBySideAndOwner(this)
        val hits = appearances.count { it.plateAppearanceResult in listOf(PlateAppearanceResult.IN_PLAY) }
        //FIXME These values can be calculated from PlateAppearanceResult. Come finish this when PlateAppearances look right
        return InningSide(0, 0, 0, 0, 0)
    }

}