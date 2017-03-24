package net.alexanderkahn.longball.service.persistence.model

import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "roster_player")
data class PersistenceRosterPlayer(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_team"), nullable = false)
        val team: PersistenceTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false)
        val player: PersistencePlayer,

        @Column(nullable = false)
        val jerseyNumber: Short,

        @Column(nullable = false)
        val startDate: OffsetDateTime,

        @Column(nullable = true)
        val endDate: OffsetDateTime? = null)