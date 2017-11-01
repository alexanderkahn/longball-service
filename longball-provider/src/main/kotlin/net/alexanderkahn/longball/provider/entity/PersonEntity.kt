package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "person")
data class PersonEntity(

        @Column(nullable = false) val first: String,
        @Column(nullable = false) val last: String,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,

        @Column(nullable = false) override val created: OffsetDateTime = OffsetDateTime.now(),
        @Column(nullable = false) override val lastModified: OffsetDateTime = created,
        @Id override val id: UUID = UUID.randomUUID()

) : BaseEntity