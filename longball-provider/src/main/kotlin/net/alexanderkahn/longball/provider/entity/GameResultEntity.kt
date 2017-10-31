package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "game_result")
data class GameResultEntity(

        @OneToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: GameEntity,

        @Column(nullable = false)
        val endTime: OffsetDateTime,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,
        @Id override val id: UUID = UUID.randomUUID()

) : BaseEntity