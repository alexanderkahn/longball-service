package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.longball.provider.entity.InningEntity
import net.alexanderkahn.longball.provider.entity.InningSideEntity
import net.alexanderkahn.longball.model.type.Side

interface InningSideRepository: LongballRepository<InningSideEntity> {
    fun findByInningAndOwner(inning: InningEntity, owner: UserEntity): List<InningSideEntity>
    fun findByInningAndSideAndOwner(inning: InningEntity, side: Side, owner: UserEntity): InningSideEntity?
}