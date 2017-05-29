package net.alexanderkahn.longball.service.persistence.entity

import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "game_result")
data class PxGameResult(

        @OneToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @Column(nullable = false)
        val endTime: OffsetDateTime,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()): OwnedIdentifiable