package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.longball.model.type.PlayResultType
import net.alexanderkahn.longball.model.type.BaseLocation
import java.util.*

data class BasepathResultDTO(val lineupPlayer: UUID, val location: BaseLocation, val result: PlayResultType)