package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LeagueRepository: LongballRepository<LeagueEntity> {
    fun findByOwnerAndNameIgnoreCaseContainingOrderByCreated(pageable: Pageable, embeddableUser: UserEntity, nameFilter: String): Page<LeagueEntity>
}