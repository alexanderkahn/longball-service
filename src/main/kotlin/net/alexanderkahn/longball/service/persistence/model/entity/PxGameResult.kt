package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
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