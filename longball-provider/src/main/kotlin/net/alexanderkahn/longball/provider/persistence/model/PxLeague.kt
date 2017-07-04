package net.alexanderkahn.longball.provider.persistence.model

import net.alexanderkahn.longball.provider.persistence.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity(name = "league")
class PxLeague(

        @Column(nullable = false) val name: String

) : BaseEntity()