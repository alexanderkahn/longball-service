package net.alexanderkahn.longball.core.assembler

import net.alexanderkahn.longball.core.persistence.model.*
import net.alexanderkahn.longball.model.*



fun PxGame.toModel(): Game {
    if (id == null || league.id == null || awayTeam.id == null || homeTeam.id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved Game")
    }
    return Game(id, league.id, awayTeam.id, homeTeam.id, startTime.toZonedDateTime())
}

fun PxLeague.toModel(): League {
    if (id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved League")
    }
    return League(id, name)
}

fun PxLineupPlayer.toModel(): LineupPlayer {
    if (id == null || game.id == null || player.id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved LineupPlayer")
    }
    return LineupPlayer(player.id, battingOrder, FieldPosition.fromNotation(fieldPosition))
}

fun PxPlayer.toModel(): Player {
    if (id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved Player")
    }
    return Player(id, first, last)
}

fun PxRosterPlayer.toModel(): RosterPlayer {
    if (id == null || team.id == null || player.id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved RosterPlayer")
    }
    return RosterPlayer(id,
            team.id,
            player.id,
            jerseyNumber,
            startDate.toZonedDateTime(),
            endDate?.toZonedDateTime())
}

fun PxTeam.toModel(): Team {
    if (id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved team")
    }
    return Team(id, abbreviation, location, nickname)
}