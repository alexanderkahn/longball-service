package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxGame
import net.alexanderkahn.longball.service.persistence.model.entity.PxPlateAppearance

interface PlateAppearanceRepository: LongballRepository<PxPlateAppearance> {
    fun findFirstByOwnerAndGameOrderByIdDesc(owner: EmbeddableUser, game: PxGame): PxPlateAppearance?
}