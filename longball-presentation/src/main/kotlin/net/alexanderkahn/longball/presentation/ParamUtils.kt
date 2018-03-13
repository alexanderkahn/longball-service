package net.alexanderkahn.longball.presentation

import net.alexanderkahn.longball.model.type.Side

fun getSideFromParam(side: String) = if ("top".equals(side, ignoreCase = true)) Side.TOP else Side.BOTTOM
