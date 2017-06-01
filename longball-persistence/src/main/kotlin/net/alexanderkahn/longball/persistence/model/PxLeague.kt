package net.alexanderkahn.longball.persistence.model

import net.alexanderkahn.longball.persistence.EmbeddableUser
import net.alexanderkahn.longball.persistence.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "league")
class PxLeague(

        @Embedded
        override val owner: EmbeddableUser,

        @Column(nullable = false) val name: String,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null

): OwnedIdentifiable