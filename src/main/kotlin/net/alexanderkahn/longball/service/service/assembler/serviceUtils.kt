package net.alexanderkahn.longball.service.service.assembler

import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.model.entity.*

fun PxInningHalf.toResult(basePathResults: List<PxBasepathResult>): PxInningHalfResult {
    return PxInningHalfResult(this,
            basePathResults.hits,
            basePathResults.walks,
            basePathResults.errors,
            basePathResults.runs)
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

//TODO: this assumes a bunch of state (results are related to appearances). It should be in the service probably so the state can be verified.
fun List<PxPlateAppearance>.getOuts(basepathResults: List<PxBasepathResult>): Int {
        val outAtPlate = mapNotNull { it.plateAppearanceResult }.count { it in arrayOf(PlateAppearanceResult.STRIKEOUT_LOOKING, PlateAppearanceResult.STRIKEOUT_SWINGING) }
        val outOnBase = basepathResults.count { it.playResult == PlayResult.OUT }
        return outAtPlate + outOnBase
}

fun List<PxBasepathResult>.getCurrentOnBase(): List<PxBasepathResult> {
    val sorted = this.sortedByDescending { it.id }
    return sorted.distinctBy { it.lineupPlayer.battingOrder }.filter { it.playResult == PlayResult.SAFE && it.location != PlayLocation.HOME }
}

val List<PxBasepathResult>.hits: Int
    get() {
        return 0 //TODO
    }

val List<PxBasepathResult>.walks: Int
    get() {
        return mapNotNull { it.gameplayEvent.plateAppearance }.distinct().count { it.plateAppearanceResult in arrayOf(PlateAppearanceResult.BASE_ON_BALLS, PlateAppearanceResult.HIT_BY_PITCH) }
    }

val List<PxBasepathResult>.errors: Int
    get() {
        return 0 //TODO
    }

val List<PxBasepathResult>.runs: Int
    get() {
        return count { it.location == PlayLocation.HOME && it.playResult == PlayResult.SAFE }
    }