package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.longball.service.model.League
import net.alexanderkahn.longball.service.persistence.model.PersistenceLeague

class LeagueAssembler: Assembler<League, PersistenceLeague> {
    override fun toModel(persistenceObject: PersistenceLeague): League {
        if (persistenceObject.id == null) {
            throw UnsupportedOperationException()
        }
        return League(persistenceObject.id, persistenceObject.name)
    }

    override fun toPersistence(modelObject: League): PersistenceLeague {
        throw UnsupportedOperationException("not implemented")
    }
}