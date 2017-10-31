package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.LeagueDTO
import net.alexanderkahn.longball.model.dto.PersonDTO
import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import net.alexanderkahn.longball.model.dto.TeamDTO
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.PersonEntity
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import net.alexanderkahn.longball.provider.entity.TeamEntity


fun LeagueEntity.toDTO(): LeagueDTO {
    return LeagueDTO(id, name)
}

fun PersonEntity.toDTO(): PersonDTO {
    return PersonDTO(id, first, last)
}

fun RosterPositionEntity.toDTO(): RosterPositionDTO {
    return RosterPositionDTO(id,
            team.id,
            player.id,
            jerseyNumber,
            startDate,
            endDate)
}

fun TeamEntity.toDTO(): TeamDTO {
    return TeamDTO(id, league.id, abbreviation, location, nickname)
}