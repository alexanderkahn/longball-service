package net.alexanderkahn.longball.provider.assembler


import net.alexanderkahn.longball.model.Inning
import net.alexanderkahn.longball.model.InningSide
import net.alexanderkahn.longball.model.PlateAppearanceResult
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.provider.entity.PxInning
import net.alexanderkahn.longball.provider.entity.PxInningSide
import net.alexanderkahn.longball.provider.repository.InningSideRepository
import net.alexanderkahn.longball.provider.repository.PlateAppearanceRepository
import net.alexanderkahn.service.base.api.security.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class InningAssembler(
        @Autowired private val inningSideRepository: InningSideRepository,
        @Autowired private val plateAppearanceRepository: PlateAppearanceRepository) {

    fun toModel(entity: PxInning): Inning {
        return Inning(entity.inningNumber, getInningSide(entity, Side.TOP) ?: throw Exception(),  getInningSide(entity, Side.BOTTOM))
    }

    private fun getInningSide(inning: PxInning, side: Side): InningSide? {
        return inningSideRepository.findByInningAndSideAndOwner(inning, side, UserContext.pxUser)?.toModel()
    }

    fun PxInningSide.toModel(): InningSide {
        val appearances = plateAppearanceRepository.findBySideAndOwner(this, UserContext.pxUser)
        val hits = appearances.count { it.plateAppearanceResult in listOf(PlateAppearanceResult.IN_PLAY) }
        //FIXME These values can be calculated from PlateAppearanceResult. Come finish this when PlateAppearances look right
        return InningSide(0, 0, 0, 0, 0)
    }

}