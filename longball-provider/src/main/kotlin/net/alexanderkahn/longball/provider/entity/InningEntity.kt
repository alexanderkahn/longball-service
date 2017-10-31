package net.alexanderkahn.longball.provider.entity


import java.util.*
import javax.persistence.*

@Entity(name = "inning")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("game_id", "inningNumber"))))
data class InningEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: GameEntity,

        @Column(nullable = false)
        val inningNumber: Int,

        @Embedded override val owner: EmbeddableUser,
        @Id override val id: UUID = UUID.randomUUID()

) : BaseEntity