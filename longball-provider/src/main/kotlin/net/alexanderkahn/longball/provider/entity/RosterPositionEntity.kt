package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "roster_position")
class RosterPositionEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_team"), nullable = false)
        val team: TeamEntity,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_person"), nullable = false)
        val player: PersonEntity,

        @Column(nullable = false) val jerseyNumber: Number,
        @Column(nullable = false) val startDate: OffsetDateTime,
        @Column(nullable = true) val endDate: OffsetDateTime? = null

) : BaseEntity()