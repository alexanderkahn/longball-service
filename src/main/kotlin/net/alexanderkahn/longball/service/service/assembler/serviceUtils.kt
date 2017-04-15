package net.alexanderkahn.longball.service.service.assembler

import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.model.entity.*

fun PxInningHalf.toResult(): PxInningHalfResult {
    return PxInningHalfResult(this,
            this.plateAppearances.hits,
            this.plateAppearances.walks,
            this.plateAppearances.errors,
            this.plateAppearances.runs)
}

val List<PxGameplayEvent>.balls: Int
    get() {
        return count { it.pitch == Pitch.BALL }
    }

val List<PxGameplayEvent>.strikes: Int
    get() {
        var strikes = 0
        forEach {
            when(it.pitch) {
                Pitch.STRIKE_LOOKING, Pitch.STRIKE_SWINGING -> strikes++
                Pitch.FOUL_TIP -> if (strikes < (LeagueRuleSet.STRIKES_PER_OUT - 1)) strikes++
                else -> {}
            }
        }
        return strikes
    }

val List<PxPlateAppearance>.outs: Int
    get() {
        val outAtPlate = mapNotNull { it.plateAppearanceResult }.count { it in arrayOf(PlateAppearanceResult.STRIKEOUT_LOOKING, PlateAppearanceResult.STRIKEOUT_SWINGING) }
        val outOnBase = flatMap { it.pitchEvents }.flatMap { it.basepathResults }.count { it.playResult == PlayResult.OUT }
        return outAtPlate + outOnBase
    }

val List<PxPlateAppearance>.currentOnBase: List<PxBasepathResult>
    get() {
        val basePath: List<PxBasepathResult> = flatMap { it.pitchEvents }.flatMap { it.basepathResults }.sortedByDescending { it.id }
        if (basePath.any { it.lineupPlayer.player.id == null }) {
            throw Exception("no player id?")
        }

        return basePath.distinctBy { it.lineupPlayer.battingOrder }.filter { it.playResult == PlayResult.SAFE && it.location != PlayLocation.HOME }
    }

val List<PxPlateAppearance>.hits: Int
    get() {
        return 0 //TODO
    }

val List<PxPlateAppearance>.walks: Int
    get() {
        return mapNotNull { it.plateAppearanceResult }.count { it in arrayOf(PlateAppearanceResult.BASE_ON_BALLS, PlateAppearanceResult.HIT_BY_PITCH) }
    }

val List<PxPlateAppearance>.errors: Int
    get() {
        return 0 //TODO
    }

val List<PxPlateAppearance>.runs: Int
    get() {
        return flatMap { it.pitchEvents }.flatMap { it.basepathResults }.count { it.location == PlayLocation.HOME && it.playResult == PlayResult.SAFE }
    }