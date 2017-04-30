package net.alexanderkahn.longball.service.model

data class Inning(val side: InningSide, val inning: Int, val outs: Int, val hits: Int, val walks: Int, val errors: Int, val runs: Int)