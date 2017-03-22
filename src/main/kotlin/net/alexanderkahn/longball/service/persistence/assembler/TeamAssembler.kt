package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.longball.service.model.Team
import net.alexanderkahn.longball.service.persistence.model.PersistenceTeam

class TeamAssembler: Assembler<Team, PersistenceTeam> {
    override fun toModel(persistenceObject: PersistenceTeam): Team {
        if (persistenceObject.id == null) {
            throw UnsupportedOperationException("Cannot convert team without Id")
        }
        return Team(id = persistenceObject.id,
                abbreviation = persistenceObject.abbreviation,
                location = persistenceObject.location,
                nickname = persistenceObject.nickname)
    }

    override fun toPersistence(modelObject: Team): PersistenceTeam {
        throw UnsupportedOperationException("not implemented")
    }
}