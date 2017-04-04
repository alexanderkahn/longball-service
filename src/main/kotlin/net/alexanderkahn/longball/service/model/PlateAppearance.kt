package net.alexanderkahn.longball.service.model

data class PlateAppearance(val pitcher: Long, val batter: Long, val onBase: List<BaseRunner>, val count: PlateAppearanceCount)