package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "game")
data class GameEntity(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_league"), nullable = false) val league: LeagueEntity,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_away_team"), nullable = false) val awayTeam: TeamEntity,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_home_team"), nullable = false) val homeTeam: TeamEntity,

        @Column(nullable = false) val startTime: OffsetDateTime,

        @OneToOne(mappedBy = "game", cascade = arrayOf(CascadeType.ALL))
        var resultEntity: GameResultEntity? = null,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,
        @Id override val id: UUID = UUID.randomUUID()


) : BaseEntity