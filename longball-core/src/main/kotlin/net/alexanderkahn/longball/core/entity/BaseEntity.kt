package net.alexanderkahn.longball.core.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

interface BaseEntity {
    val created: OffsetDateTime
    var lastModified: OffsetDateTime
    val id: UUID
}

interface OwnedEntity : BaseEntity {
    val owner: UserEntity

}
class UpdateLastModifiedListener {
    @PrePersist
    @PreUpdate
    fun <T : BaseEntity>updateLastModified(reference: T) {
        reference.lastModified = OffsetDateTime.now()
    }
}