package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.longball.service.model.LineupPosition
import net.alexanderkahn.longball.service.persistence.model.PersistenceLineupPosition

class LineupPositionAssembler: Assembler<LineupPosition, PersistenceLineupPosition> {
    override fun toModel(persistenceObject: PersistenceLineupPosition): LineupPosition {
        if (persistenceObject.id == null || persistenceObject.game.id == null || persistenceObject.player.id == null) {
            throw UnsupportedOperationException("Cannot convert unsaved LineupPosition")
        }
        return LineupPosition(persistenceObject.player.id, persistenceObject.battingOrder, persistenceObject.fieldPosition)
    }

    override fun toPersistence(modelObject: LineupPosition): PersistenceLineupPosition {
        throw UnsupportedOperationException("not implemented")
    }
}