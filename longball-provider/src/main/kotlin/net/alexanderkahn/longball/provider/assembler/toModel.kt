package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.*
import net.alexanderkahn.longball.provider.entity.*


fun GameEntity.toModel(): GameDTO {
    return GameDTO(id, league.id, awayTeam.id, homeTeam.id, startTime)
}

fun LeagueEntity.toModel(): LeagueDTO {
    return LeagueDTO(id, name)
}

fun LineupPositionEntity.toModel(): LineupPositionDTO {
    return LineupPositionDTO(player.id, battingOrder, FieldPosition.fromNotation(fieldPosition))
}

fun PersonEntity.toModel(): PersonDTO {
    return PersonDTO(id, first, last)
}

fun PlayerEntity.toModel(): PlayerDTO {
    return PlayerDTO(id,
            team.id,
            player.id,
            jerseyNumber,
            startDate,
            endDate)
}

fun TeamEntity.toModel(): TeamDTO {
    return TeamDTO(id, league.id, abbreviation, location, nickname)
}

fun PlateAppearanceEntity.toModel(): PlateAppearanceDTO {
    return PlateAppearanceDTO(this.plateAppearanceResult)
}