package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.longball.model.type.FieldPosition
import java.util.*

data class LineupPositionDTO(val player: UUID, val battingOrder: Int, val fieldPosition: FieldPosition)

