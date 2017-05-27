package net.alexanderkahn.longball.service.model

data class Inning(val inningNumber: Int, val top: InningSide, val bottom: InningSide? = null)