package net.alexanderkahn.longball.provider.persistence.model

import net.alexanderkahn.longball.provider.persistence.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity(name = "team")
data class PxTeam(

        @Column(nullable = false) val abbreviation: String,

        @Column(nullable = false) val location: String,

        @Column(nullable = false) val nickname: String

) : BaseEntity()