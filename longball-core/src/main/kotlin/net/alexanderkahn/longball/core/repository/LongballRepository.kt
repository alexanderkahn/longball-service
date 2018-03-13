package net.alexanderkahn.longball.core.repository

import net.alexanderkahn.longball.core.entity.BaseEntity
import net.alexanderkahn.longball.core.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import java.util.*

@NoRepositoryBean
interface LongballRepository<Entity: BaseEntity>: Repository<Entity, UUID>, JpaSpecificationExecutor<Entity> {
    fun <S : Entity> save(entity: S): S
    fun existsById(id: UUID): Boolean
    fun findByIdAndOwner(id: UUID, currentUser: UserEntity): Entity?
    fun findByOwnerOrderByCreated(currentUser: UserEntity, pageable: Pageable): Page<Entity>
    fun deleteById(id: UUID)
}
