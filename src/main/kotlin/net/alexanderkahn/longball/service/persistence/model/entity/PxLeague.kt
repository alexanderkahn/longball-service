package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity(name = "league")
data class PxLeague(

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long?,

        @Embedded
        override val owner: EmbeddableUser,

        @javax.persistence.Column(nullable = false)
        val name: String
): OwnedIdentifiable