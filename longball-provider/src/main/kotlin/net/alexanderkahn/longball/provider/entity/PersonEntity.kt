package net.alexanderkahn.longball.provider.entity

import java.util.*
import javax.persistence.*

@Entity(name = "person")
data class PersonEntity(

        @Column(nullable = false) val first: String,
        @Column(nullable = false) val last: String,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,
        @Id override val id: UUID = UUID.randomUUID()

) : BaseEntity