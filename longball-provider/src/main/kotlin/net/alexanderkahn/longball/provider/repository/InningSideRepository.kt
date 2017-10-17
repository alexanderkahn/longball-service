package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.InningEntity
import net.alexanderkahn.longball.provider.entity.InningSideEntity
import net.alexanderkahn.longball.model.Side

interface InningSideRepository: LongballRepository<InningSideEntity> {
    fun findByInningAndOwner(inning: InningEntity, owner: EmbeddableUser): List<InningSideEntity>
    fun findByInningAndSideAndOwner(inning: InningEntity, side: Side, owner: EmbeddableUser): InningSideEntity?
}