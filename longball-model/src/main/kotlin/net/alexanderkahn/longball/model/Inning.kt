package net.alexanderkahn.longball.model

data class Inning(val inningNumber: Int, val top: InningSide, val bottom: InningSide? = null)