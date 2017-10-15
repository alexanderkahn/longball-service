package net.alexanderkahn.longball.provider.entity

import javax.persistence.Column
import javax.persistence.Entity

@Entity(name = "person")
data class PxPerson(

        @Column(nullable = false) val first: String,
        @Column(nullable = false) val last: String

) : BaseEntity()