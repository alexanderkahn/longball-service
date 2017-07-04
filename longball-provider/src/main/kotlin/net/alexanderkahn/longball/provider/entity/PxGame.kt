package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "game")
data class PxGame(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_league"), nullable = false) val league: PxLeague,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_away_team"), nullable = false) val awayTeam: PxTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_home_team"), nullable = false) val homeTeam: PxTeam,

        @Column(nullable = false) val startTime: OffsetDateTime,

        @OneToOne(mappedBy = "game", cascade = arrayOf(CascadeType.ALL))
        var result: PxGameResult? = null

): BaseEntity()