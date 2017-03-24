package net.alexanderkahn.longball.service.persistence.model

import org.springframework.data.annotation.PersistenceConstructor
import java.time.Instant
import javax.persistence.*

@Table(
        uniqueConstraints=
        arrayOf(UniqueConstraint(name = "uk_team_player", columnNames=arrayOf("team_id", "player_id")))
)
@Entity(name = "roster_player")
data class PersistenceRosterPlayer @PersistenceConstructor constructor(
        @Id
        @GeneratedValue
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
        val startDate: Instant,

        @Column(nullable = true)
        val endDate: Instant? = null)