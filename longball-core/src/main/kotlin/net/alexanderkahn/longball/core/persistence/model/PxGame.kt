package net.alexanderkahn.longball.core.persistence.model

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.OwnedIdentifiable
import java.time.OffsetDateTime
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "game")
data class PxGame(
        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_league"), nullable = false) val league: PxLeague,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_away_team"), nullable = false) val awayTeam: PxTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_home_team"), nullable = false) val homeTeam: PxTeam,

        @Column(nullable = false) val startTime: OffsetDateTime,

        @OneToOne(mappedBy = "game", cascade = arrayOf(CascadeType.ALL))
        var result: PxGameResult? = null,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null

): OwnedIdentifiable