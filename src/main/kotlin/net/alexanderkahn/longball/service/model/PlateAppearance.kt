package net.alexanderkahn.longball.service.model

data class PlateAppearance(val pitcher: Long, val batter: Long, val inning: Inning, val onBase: List<BaseRunner>, val count: PlateAppearanceCount)