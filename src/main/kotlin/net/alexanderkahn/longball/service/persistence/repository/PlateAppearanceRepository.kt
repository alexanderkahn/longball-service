package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.PersistenceGame
import net.alexanderkahn.longball.service.persistence.model.PersistencePlateAppearance

interface PlateAppearanceRepository: LongballRepository<PersistencePlateAppearance> {
    fun findLastByOwnerAndGame(owner: EmbeddableUser, game: PersistenceGame): PersistencePlateAppearance?
}