package net.alexanderkahn.longball.core.persistence.model

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.OwnedIdentifiable
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