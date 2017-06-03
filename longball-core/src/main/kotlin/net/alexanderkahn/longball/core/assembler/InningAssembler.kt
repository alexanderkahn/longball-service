package net.alexanderkahn.longball.core.assembler


import net.alexanderkahn.longball.model.Inning
import net.alexanderkahn.longball.model.InningSide
import net.alexanderkahn.longball.model.PlateAppearanceResult
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.core.persistence.model.PxInning
import net.alexanderkahn.longball.core.persistence.model.PxInningSide
import net.alexanderkahn.longball.core.persistence.repository.InningSideRepository
import net.alexanderkahn.longball.core.persistence.repository.PlateAppearanceRepository
import net.alexanderkahn.servicebase.core.security.UserContext

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
        return inningSideRepository.findByInningAndSideAndOwner(inning, side.ordinal, UserContext.pxUser)?.toModel()
    }

    fun PxInningSide.toModel(): InningSide {
        val appearances = plateAppearanceRepository.findBySideAndOwner(this, UserContext.pxUser)
        val hits = appearances.count { it.plateAppearanceResult in listOf(PlateAppearanceResult.IN_PLAY.ordinal) }
        //FIXME These values can be calculated from PlateAppearanceResult. Come finish this when PlateAppearances look right
        return InningSide(0, 0, 0, 0, 0)
    }

}