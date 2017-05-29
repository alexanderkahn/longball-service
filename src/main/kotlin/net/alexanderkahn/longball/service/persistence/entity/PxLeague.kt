package net.alexanderkahn.longball.service.persistence.entity

import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "league")
class PxLeague(

        @Column(nullable = false) val name: String,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable