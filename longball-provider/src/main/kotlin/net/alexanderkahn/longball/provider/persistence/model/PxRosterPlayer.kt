package net.alexanderkahn.longball.provider.persistence.model

import net.alexanderkahn.longball.provider.persistence.EmbeddableUser
import net.alexanderkahn.longball.provider.persistence.OwnedIdentifiable
import java.time.OffsetDateTime
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "roster_player")
class PxRosterPlayer(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_team"), nullable = false) val team: PxTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false) val player: PxPlayer,

        @Column(nullable = false) val jerseyNumber: Int,

        @Column(nullable = false) val startDate: OffsetDateTime,

        @Column(nullable = true) val endDate: OffsetDateTime? = null,

        @Id
        override @GeneratedValue(strategy = IDENTITY) val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser
): OwnedIdentifiable