package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "player")
class PxPlayer(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_team"), nullable = false)
        val team: PxTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_person"), nullable = false)
        val player: PxPerson,

        @Column(nullable = false) val jerseyNumber: Int,
        @Column(nullable = false) val startDate: OffsetDateTime,
        @Column(nullable = true) val endDate: OffsetDateTime? = null

) : BaseEntity()