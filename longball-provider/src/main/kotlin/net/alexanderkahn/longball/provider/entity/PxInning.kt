package net.alexanderkahn.longball.provider.entity


import javax.persistence.*

@Entity(name = "inning")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("game_id", "inningNumber"))))
data class PxInning(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @Column(nullable = false)
        val inningNumber: Int

        ) : BaseEntity()