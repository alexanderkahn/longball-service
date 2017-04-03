package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.base.servicebase.model.User
import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.model.*

fun User.toPersistence(): EmbeddableUser {
    return EmbeddableUser(issuer, userId)
}

fun PersistenceGame.toModel(): Game {
    if (id == null || league.id == null || awayTeam.id == null || homeTeam.id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved Game")
    }
    return Game(id, league.id, awayTeam.id, homeTeam.id, startTime.toZonedDateTime())
}

fun PersistenceLeague.toModel(): League {
    if (id == null) {
        throw UnsupportedOperationException("Cannot convert unsa")
    }
    return League(id, name)
}

fun PersistenceLineupPosition.toModel(): LineupPosition {
    if (id == null || game.id == null || player.id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved LineupPosition")
    }
    return LineupPosition(player.id, battingOrder, fieldPosition)
}

fun PersistencePlayer.toModel(): Player {
    if (id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved Player")
    }
    return Player(id, first, last)
}

fun PersistenceRosterPlayer.toModel(): RosterPlayer {
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

fun PersistenceTeam.toModel(): Team {
    if (id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved team")
    }
    return Team(id, abbreviation, location, nickname)
}

fun PersistencePlateAppearance.toModel(pitcher: PersistencePlayer): PlateAppearance {
    if (id == null || pitcher.id == null ||  batter.id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved plate appearance")
    }
    //TODO: onBase
    //TODO: pitch count
    return PlateAppearance(pitcher.id, batter.id, listOf(), PlateAppearanceCount(0, 0))
}