package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "player")
data class PxPlayer(
        @Column(nullable = false) val first: String,

        @Column(nullable = false) val last: String,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable