package net.alexanderkahn.longball.service.model

data class InningDetails(val inning: Int, val half: InningHalf) {

    val gameEvents: List<GameEvent> = mutableListOf()

}

class RosterEvent: GameEvent

open class GameplayEvent(val baseRunningEvents: List<BaseRunningEvent>)

class PitchEvent(val pitch: Pitch, baseRunningEvents: List<BaseRunningEvent>): GameplayEvent(baseRunningEvents)



