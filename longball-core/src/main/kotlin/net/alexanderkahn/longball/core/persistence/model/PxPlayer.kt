package net.alexanderkahn.longball.core.persistence.model

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "player")
data class PxPlayer(

        @Embedded
        override val owner: EmbeddableUser,

        @Column(nullable = false) val first: String,

        @Column(nullable = false) val last: String,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null
): OwnedIdentifiable