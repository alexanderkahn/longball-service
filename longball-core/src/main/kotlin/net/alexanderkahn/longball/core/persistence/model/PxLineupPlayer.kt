package net.alexanderkahn.longball.core.persistence.model

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "lineup_player")
data class PxLineupPlayer(

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false) val game: PxGame, //TODO: this should link to a GameLineup or something rather than storing game and side

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false) val player: PxPlayer,

        @Column(nullable = false) val side: Int,

        @Column(nullable = false) val battingOrder: Int,

        @Column(nullable = false) val fieldPosition: Int,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null

): OwnedIdentifiable