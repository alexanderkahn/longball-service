package net.alexanderkahn.longball.service.model

data class InningSummary(val innings: List<Inning>) {

    val totals: Inning
        get() = Inning(innings.map{it.top }.sum(), innings.map{it.bottom }.sum())

}