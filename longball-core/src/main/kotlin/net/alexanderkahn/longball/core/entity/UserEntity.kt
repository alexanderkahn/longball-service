package net.alexanderkahn.longball.core.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Id

@Entity(name = "longball_user")
@EntityListeners(UpdateLastModifiedListener::class)
data class UserEntity(
        @Column(nullable = false) val issuer: String,
        @Column(nullable = false) val username: String,
        @Column(nullable = false) var displayName: String,
        @Column(nullable = false) override val created: OffsetDateTime = OffsetDateTime.now(),
        @Column(nullable = false) override var lastModified: OffsetDateTime = created,
        @Id override val id: UUID = UUID.randomUUID()
) : BaseEntity
