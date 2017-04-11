package net.alexanderkahn.longball.service.service.assembler

import net.alexanderkahn.base.servicebase.model.User
import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.*

fun User.toPersistence(): EmbeddableUser {
    return EmbeddableUser(issuer, userId)
}

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
    return LineupPlayer(player.id, battingOrder, fieldPosition)
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

fun PxPlateAppearance.toModel(pitcher: PxPlayer, outs: Int, hits: Int, walks: Int, errors: Int, runs: Int, currentOnBase: List<PxBasePathResult>): PlateAppearance {
    if (id == null || pitcher.id == null || batter.id == null) {
        throw UnsupportedOperationException("Cannot convert unsaved plate appearance")
    }
    //TODO: Once outs can come at the plate or on the basepath, this might get more complicated
    val inning = Inning(inningHalf.half, inningHalf.inning.inningNumber, outs, hits, walks, errors, runs)
    val onBase = currentOnBase.map{ it.toBaserunner() }
    return PlateAppearance(pitcher.id, batter.id, inning, onBase, pitchEvents.toPlateAppearanceCount(), plateAppearanceResult)
}

private fun PxBasePathResult.toBaserunner(): BaseRunner {
    if (lineupPlayer.player.id == null) {
        throw UnsupportedOperationException("Not saved")
    }
    return BaseRunner(location, lineupPlayer.player.id)
}

fun List<PxGameplayEvent>.toPlateAppearanceCount(): PlateAppearanceCount {
    var balls = 0
    var strikes = 0
    this.map { it.pitch }.forEach {
        when(it) {
            Pitch.BALL -> balls++
            Pitch.STRIKE_LOOKING, Pitch.STRIKE_SWINGING -> strikes++
            Pitch.FOUL_TIP -> if (strikes < (LeagueRuleSet.STRIKES_PER_OUT - 1)) strikes++
            else -> {}
        }
    }
    return PlateAppearanceCount(balls, strikes)
}