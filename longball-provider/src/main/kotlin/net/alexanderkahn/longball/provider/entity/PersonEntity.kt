package net.alexanderkahn.longball.provider.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "person")
data class PersonEntity(

        @Column(nullable = false) val first: String,
        @Column(nullable = false) val last: String,

        @Embedded override val owner: EmbeddableUser,
        @Id override val id: UUID = UUID.randomUUID()

) : BaseEntity