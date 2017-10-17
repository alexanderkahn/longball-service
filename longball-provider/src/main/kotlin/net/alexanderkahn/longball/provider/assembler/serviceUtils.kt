package net.alexanderkahn.longball.provider.assembler

//FIXME
//fun InningSideEntity.toResult(basePathResults: List<PxBasepathResult>): PxSideResult {
//    return PxSideResult(this,
//            basePathResults.hits,
//            basePathResults.walks,
//            basePathResults.errors,
//            basePathResults.runs)
//}
//
//val List<PxGameplayEvent>.balls: Int
//    get() {
//        return count { it.pitch == PitchType.BALL }
//    }
//
//val List<PxGameplayEvent>.strikes: Int
//    get() {
//        var strikes = 0
//        forEach {
//            when(it.pitch) {
//                PitchType.STRIKE_LOOKING, PitchType.STRIKE_SWINGING -> strikes++
//                PitchType.FOUL_TIP -> if (strikes < (LeagueRuleSetDTO.strikesPerOut - 1)) strikes++
//                else -> {}
//            }
//        }
//        return strikes
//    }
//
////TODO: this assumes a bunch of state (results are related to appearances). It should be in the service probably so the state can be verified.
//fun List<PlateAppearanceEntity>.getOuts(basepathResults: List<PxBasepathResult>): Int {
//        val outAtPlate = mapNotNull { it.plateAppearanceResult }.count { it in arrayOf(PlateAppearanceResultType.STRIKEOUT_LOOKING, PlateAppearanceResultType.STRIKEOUT_SWINGING) }
//        val outOnBase = basepathResults.count { it.playResult == PlayResultType.OUT }
//        return outAtPlate + outOnBase
//}
//
//fun List<PxBasepathResult>.getCurrentOnBase(): List<PxBasepathResult> {
//    val sorted = this.sortedByDescending { it.id }
//    return sorted.distinctBy { it.lineupPlayer.battingOrder }.filter { it.playResult == PlayResultType.SAFE && it.location != BaseLocation.HOME }
//}
//
//val List<PxBasepathResult>.hits: Int
//    get() {
//        return 0 //TODO
//    }
//
//val List<PxBasepathResult>.walks: Int
//    get() {
//        return mapNotNull { it.gameplayEvent.plateAppearance }.distinct().count { it.plateAppearanceResult in arrayOf(PlateAppearanceResultType.BASE_ON_BALLS, PlateAppearanceResultType.HIT_BY_PITCH) }
//    }
//
//val List<PxBasepathResult>.errors: Int
//    get() {
//        return 0 //TODO
//    }
//
//val List<PxBasepathResult>.runs: Int
//    get() {
//        return count { it.location == BaseLocation.HOME && it.playResult == PlayResultType.SAFE }
//    }