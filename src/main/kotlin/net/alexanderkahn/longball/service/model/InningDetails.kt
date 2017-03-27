package net.alexanderkahn.longball.service.model

data class InningDetails(val inning: Int, val half: InningHalf) {

    val gameEvents: List<GameEvent> = mutableListOf()

}



