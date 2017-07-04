package net.alexanderkahn.longball.provider.persistence.model

import net.alexanderkahn.longball.provider.persistence.EmbeddableUser
import net.alexanderkahn.longball.provider.persistence.OwnedIdentifiable
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