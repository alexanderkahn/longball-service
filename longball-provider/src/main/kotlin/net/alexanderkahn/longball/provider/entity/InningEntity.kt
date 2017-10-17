package net.alexanderkahn.longball.provider.entity


import javax.persistence.*

@Entity(name = "inning")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("game_id", "inningNumber"))))
data class InningEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: GameEntity,

        @Column(nullable = false)
        val inningNumber: Int

        ) : BaseEntity()