package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "team")
data class TeamEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_league"), nullable = false)
        val league: LeagueEntity,

        @Column(nullable = false) val abbreviation: String,
        @Column(nullable = false) val location: String,
        @Column(nullable = false) val nickname: String,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,

        @Column(nullable = false) val created: OffsetDateTime = OffsetDateTime.now(),
        @Column(nullable = false) val lastModified: OffsetDateTime = created,
        @Id override val id: UUID = UUID.randomUUID()

) : BaseEntity