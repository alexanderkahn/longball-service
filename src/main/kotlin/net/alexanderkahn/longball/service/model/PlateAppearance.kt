package net.alexanderkahn.longball.service.model

data class PlateAppearance(val pitcher: Long, val batter: Long, val inning: Inning, val outs: Int, val onBase: List<BaseRunner>, val count: PlateAppearanceCount, val plateAppearanceResult: PlateAppearanceResult?)