package net.alexanderkahn.longball.provider.persistence.model

import net.alexanderkahn.longball.provider.persistence.BaseEntity
import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "roster_player")
class PxRosterPlayer(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_team"), nullable = false) val team: PxTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false) val player: PxPlayer,

        @Column(nullable = false) val jerseyNumber: Int,

        @Column(nullable = false) val startDate: OffsetDateTime,

        @Column(nullable = true) val endDate: OffsetDateTime? = null

) : BaseEntity()