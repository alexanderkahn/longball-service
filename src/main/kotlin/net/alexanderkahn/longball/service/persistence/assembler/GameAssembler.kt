package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.longball.service.model.Game
import net.alexanderkahn.longball.service.persistence.model.PersistenceGame

class GameAssembler: Assembler<Game, PersistenceGame> {
    override fun toModel(persistenceObject: PersistenceGame): Game {
        if (persistenceObject.id == null || persistenceObject.league.id == null || persistenceObject.awayTeam.id == null || persistenceObject.homeTeam.id == null) {
            throw UnsupportedOperationException("Cannot convert unsaved Game")
        }
        return Game(persistenceObject.id,
                persistenceObject.league.id,
                persistenceObject.awayTeam.id,
                persistenceObject.homeTeam.id,
                persistenceObject.startTime.toZonedDateTime())
    }

    override fun toPersistence(modelObject: Game): PersistenceGame {
        throw UnsupportedOperationException("not implemented")
    }
}