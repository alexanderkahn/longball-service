package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.*
import net.alexanderkahn.longball.provider.entity.*


fun GameEntity.toModel(): Game {
    return Game(id, league.id, awayTeam.id, homeTeam.id, startTime)
}

fun LeagueEntity.toModel(): League {
    return League(id, name)
}

fun LineupPositionEntity.toModel(): LineupPlayer {
    return LineupPlayer(player.id, battingOrder, FieldPosition.fromNotation(fieldPosition))
}

fun PersonEntity.toModel(): Person {
    return Person(id, first, last)
}

fun PlayerEntity.toModel(): Player {
    return Player(id,
            team.id,
            player.id,
            jerseyNumber,
            startDate,
            endDate)
}

fun TeamEntity.toModel(): Team {
    return Team(id, league.id, abbreviation, location, nickname)
}

fun PlateAppearanceEntity.toModel(): PlateAppearance {
    return PlateAppearance(this.plateAppearanceResult)
}