package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository

//TODO: At some point, limit these repositories so they can't access findAll() etc anymore.
// This should work for free with a "base" interface, but may be broken by the kotlin -> java compilation
// (apparently depends on exact method signature match). https://spring.io/blog/2011/07/27/fine-tuning-spring-data-repositories/

@NoRepositoryBean
interface LongballRepository<PersistenceObject: OwnedIdentifiable>: PagingAndSortingRepository<PersistenceObject, Long> {

    override fun <S : PersistenceObject> save(entity: S): S

    fun findByIdAndOwner(id: Long, currentUser: EmbeddableUser): PersistenceObject

    fun findByOwner(pageable: Pageable, currentUser: EmbeddableUser): Page<PersistenceObject>

}
