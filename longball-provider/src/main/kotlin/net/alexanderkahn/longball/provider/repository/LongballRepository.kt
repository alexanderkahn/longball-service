package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.BaseEntity
import net.alexanderkahn.longball.provider.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

//TODO: At some point, limit these repositories so they can't access findAll() etc anymore.
// This should work for free with a "base" interface, but may be broken by the kotlin -> java compilation
// (apparently depends on exact method signature match). https://spring.io/blog/2011/07/27/fine-tuning-spring-data-repositories/

@NoRepositoryBean
interface LongballRepository<PersistenceObject: BaseEntity>: PagingAndSortingRepository<PersistenceObject, UUID> {

    override fun <S : PersistenceObject> save(entity: S): S

    fun findByIdAndOwner(id: UUID, currentUser: UserEntity): PersistenceObject?

    fun findByOwnerOrderByCreated(pageable: Pageable, currentUser: UserEntity): Page<PersistenceObject>

}
