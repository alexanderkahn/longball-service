package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "game_result")
data class PxGameResult(

        @OneToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @Column(nullable = false)
        val endTime: OffsetDateTime

) : BaseEntity()