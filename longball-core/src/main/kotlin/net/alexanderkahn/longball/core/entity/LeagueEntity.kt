package net.alexanderkahn.longball.core.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "league")
@EntityListeners(UpdateLastModifiedListener::class)
class LeagueEntity(
        @Column(nullable = false) val name: String,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,

        @Column(nullable = false) override val created: OffsetDateTime = OffsetDateTime.now(),
        @Column(nullable = false) override var lastModified: OffsetDateTime = created,
        @Id override val id: UUID = UUID.randomUUID()
) : OwnedEntity