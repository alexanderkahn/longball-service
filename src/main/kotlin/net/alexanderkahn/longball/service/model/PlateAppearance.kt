package net.alexanderkahn.longball.service.model

data class PlateAppearance(val pitching: Long, val batting: Long, val onBase: List<BaseRunner>, val count: PlateAppearanceCount)