package net.alexanderkahn.longball.provider.entity

import javax.persistence.Column
import javax.persistence.Entity

@Entity(name = "league")
class PxLeague(

        @Column(nullable = false) val name: String

) : BaseEntity()