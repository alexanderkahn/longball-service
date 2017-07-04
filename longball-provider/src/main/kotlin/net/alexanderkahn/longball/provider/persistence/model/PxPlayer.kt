package net.alexanderkahn.longball.provider.persistence.model

import net.alexanderkahn.longball.provider.persistence.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity(name = "player")
data class PxPlayer(

        @Column(nullable = false) val first: String,

        @Column(nullable = false) val last: String

) : BaseEntity()