package net.alexanderkahn.longball.service.service.assembler

import net.alexanderkahn.longball.service.model.PlateAppearanceResult
import net.alexanderkahn.longball.service.model.PlayLocation
import net.alexanderkahn.longball.service.model.PlayResult
import net.alexanderkahn.longball.service.persistence.model.entity.PxBasePathResult
import net.alexanderkahn.longball.service.persistence.model.entity.PxInningHalf
import net.alexanderkahn.longball.service.persistence.model.entity.PxInningHalfResult
import net.alexanderkahn.longball.service.persistence.model.entity.PxPlateAppearance

fun PxInningHalf.toResult(): PxInningHalfResult {
    return PxInningHalfResult(0, this, null)
}

fun List<PxPlateAppearance>.toOuts(): Int {
    //TODO: Once outs can come at the plate or on the basepath, this might get more complicated
    val outAtPlate = mapNotNull { it.plateAppearanceResult }.count { it in arrayOf(PlateAppearanceResult.STRIKEOUT_LOOKING, PlateAppearanceResult.STRIKEOUT_SWINGING) }
    val outOnBase = flatMap { it.pitchEvents }.flatMap { it.basepathResults }.count { it.playResult == PlayResult.OUT }
    return outAtPlate + outOnBase
}

fun List<PxPlateAppearance>.toCurrentOnBase(): List<PxBasePathResult> {
    val basePath: List<PxBasePathResult> =  flatMap { it.pitchEvents }.flatMap { it.basepathResults }.sortedByDescending { it.id }
    if (basePath.any{it.lineupPlayer.player.id == null}) {
        throw Exception("no player id?")
    }

    return basePath.distinctBy { it.lineupPlayer.battingOrder }.filter { it.playResult == PlayResult.SAFE && it.location != PlayLocation.HOME }
}