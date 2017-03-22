package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.longball.service.model.Player
import net.alexanderkahn.longball.service.persistence.model.PersistencePlayer

class PlayerAssembler: Assembler<Player, PersistencePlayer> {
    override fun toModel(persistenceObject: PersistencePlayer): Player {
        if (persistenceObject.id == null) {
            throw UnsupportedOperationException("Cannot convert unsaved Player")
        }
        return Player(persistenceObject.id, persistenceObject.first, persistenceObject.last)
    }

    override fun toPersistence(modelObject: Player): PersistencePlayer {
        throw UnsupportedOperationException("not implemented")
    }
}