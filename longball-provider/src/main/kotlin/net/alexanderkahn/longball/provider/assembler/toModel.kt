package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.*
import net.alexanderkahn.longball.provider.persistence.model.*


fun PxGame.toModel(): Game {
    return Game(id, league.id, awayTeam.id, homeTeam.id, startTime.toZonedDateTime())
}

fun PxLeague.toModel(): League {
    return League(id, name)
}

fun PxLineupPlayer.toModel(): LineupPlayer {
    return LineupPlayer(player.id, battingOrder, FieldPosition.fromNotation(fieldPosition))
}

fun PxPlayer.toModel(): Player {
    return Player(id, first, last)
}

fun PxRosterPlayer.toModel(): RosterPlayer {
    return RosterPlayer(id,
            team.id,
            player.id,
            jerseyNumber,
            startDate.toZonedDateTime(),
            endDate?.toZonedDateTime())
}

fun PxTeam.toModel(): Team {
    return Team(id, abbreviation, location, nickname)
}

fun PxPlateAppearance.toModel(): PlateAppearance {
    return PlateAppearance(this.plateAppearanceResult)
}