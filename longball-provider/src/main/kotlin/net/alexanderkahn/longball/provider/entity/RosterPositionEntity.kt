package net.alexanderkahn.longball.provider.entity

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "roster_position")
class RosterPositionEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_team"), nullable = false)
        val team: TeamEntity,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_person"), nullable = false)
        val player: PersonEntity,

        @Column(nullable = false) val jerseyNumber: Int,
        @Column(nullable = false) val startDate: LocalDate,
        @Column(nullable = true) val endDate: LocalDate? = null,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,

        @Column(nullable = false) override val created: OffsetDateTime = OffsetDateTime.now(),
        @Column(nullable = false) override val lastModified: OffsetDateTime = created,
        @Id override val id: UUID = UUID.randomUUID()

) : BaseEntity