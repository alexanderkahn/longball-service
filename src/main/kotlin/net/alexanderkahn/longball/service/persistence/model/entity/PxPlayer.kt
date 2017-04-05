package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "player")
data class PxPlayer(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser,

        @Column(nullable = false)
        val first: String,

        @Column(nullable = false)
        val last: String
): OwnedIdentifiable