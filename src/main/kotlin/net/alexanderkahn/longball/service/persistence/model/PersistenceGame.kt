package net.alexanderkahn.longball.service.persistence.model

import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "game")
data class PersistenceGame(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_league"), nullable = false)
        val league: PersistenceLeague,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_away_team"), nullable = false)
        val awayTeam: PersistenceTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_home_team"), nullable = false)
        val homeTeam: PersistenceTeam,

        @Column(nullable = false)
        val startTime: OffsetDateTime
): OwnedIdentifiable