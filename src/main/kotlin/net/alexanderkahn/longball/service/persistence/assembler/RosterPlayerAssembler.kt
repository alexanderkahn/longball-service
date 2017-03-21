package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.longball.service.model.RosterPlayer
import net.alexanderkahn.longball.service.persistence.model.PersistenceRosterPlayer
import java.util.*

class RosterPlayerAssembler:  Assembler<RosterPlayer, PersistenceRosterPlayer> {

    override fun toModel(persistenceObject: PersistenceRosterPlayer): RosterPlayer {
        if (persistenceObject.id == null) {
            throw UnsupportedOperationException("Cannot convert unsaved RosterPlayer")
        }
        return RosterPlayer(persistenceObject.id,
                persistenceObject.teamId,
                persistenceObject.playerId,
                persistenceObject.jerseyNumber,
                persistenceObject.startDate.atZone(TimeZone.getDefault().toZoneId()),
                persistenceObject.endDate?.atZone(TimeZone.getDefault().toZoneId()))
    }

    override fun toPersistence(modelObject: RosterPlayer): PersistenceRosterPlayer {
        throw UnsupportedOperationException("not implemented")
    }
}