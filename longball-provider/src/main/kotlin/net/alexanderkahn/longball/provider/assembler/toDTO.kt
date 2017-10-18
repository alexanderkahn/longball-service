package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.*
import net.alexanderkahn.longball.model.type.FieldPosition
import net.alexanderkahn.longball.provider.entity.*


fun GameEntity.toDTO(): GameDTO {
    return GameDTO(id, league.id, awayTeam.id, homeTeam.id, startTime)
}

fun LeagueEntity.toDTO(): LeagueDTO {
    return LeagueDTO(id, name)
}

fun LineupPositionEntity.toDTO(): LineupPositionDTO {
    return LineupPositionDTO(player.id, battingOrder, FieldPosition.fromNotation(fieldPosition))
}

fun PersonEntity.toDTO(): PersonDTO {
    return PersonDTO(id, first, last)
}

fun PlayerEntity.toDTO(): PlayerDTO {
    return PlayerDTO(id,
            team.id,
            player.id,
            jerseyNumber,
            startDate,
            endDate)
}

fun TeamEntity.toDTO(): TeamDTO {
    return TeamDTO(id, league.id, abbreviation, location, nickname)
}

fun PlateAppearanceEntity.toDTO(): PlateAppearanceDTO {
    return PlateAppearanceDTO(this.plateAppearanceResult)
}