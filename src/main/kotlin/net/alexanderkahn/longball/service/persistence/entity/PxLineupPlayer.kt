package net.alexanderkahn.longball.service.persistence.entity

import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.Side
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "lineup_player")
data class PxLineupPlayer(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false) val game: PxGame, //TODO: this should link to a GameLineup or something rather than storing game and side

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false) val player: PxPlayer,

        @Column(nullable = false) val side: Side,

        @Column(nullable = false) val battingOrder: Int,

        @Column(nullable = false) val fieldPosition: FieldPosition,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable