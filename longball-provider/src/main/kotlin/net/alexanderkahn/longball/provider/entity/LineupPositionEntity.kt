package net.alexanderkahn.longball.provider.entity

import javax.persistence.*

@Entity(name = "lineup_player")
data class LineupPositionEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false) val game: GameEntity, //TODO: this should link to a GameLineup or something rather than storing game and side

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false) val player: PersonEntity, //TODO: this should link to a player not a person

        @Column(nullable = false) val side: Int,
        @Column(nullable = false) val battingOrder: Int,
        @Column(nullable = false) val fieldPosition: Int

) : BaseEntity()