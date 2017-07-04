package net.alexanderkahn.longball.provider.entity

import javax.persistence.*

@Entity(name = "lineup_player")
data class PxLineupPlayer(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false) val game: PxGame, //TODO: this should link to a GameLineup or something rather than storing game and side

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false) val player: PxPlayer,

        @Column(nullable = false) val side: Int,

        @Column(nullable = false) val battingOrder: Int,

        @Column(nullable = false) val fieldPosition: Int

) : BaseEntity()