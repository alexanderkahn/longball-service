package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxGame
import net.alexanderkahn.longball.service.persistence.model.entity.PxPlateAppearance

interface PlateAppearanceRepository: LongballRepository<PxPlateAppearance> {
    fun findFirstByGameAndOwnerOrderByIdDesc(game: PxGame, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxPlateAppearance?
    fun findByGameAndInningAndHalfAndOwner(game: PxGame, inning: Short, half: InningHalf, owner: EmbeddableUser = UserContext.getPersistenceUser()): MutableList<PxPlateAppearance>
}