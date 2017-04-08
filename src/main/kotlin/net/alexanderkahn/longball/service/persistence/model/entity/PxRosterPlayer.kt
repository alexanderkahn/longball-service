package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import java.time.OffsetDateTime
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "roster_player")
class PxRosterPlayer(
        @Id
        override @GeneratedValue(strategy = IDENTITY)
        val id: Long? = null,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_team"), nullable = false)
        val team: PxTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false)
        val player: PxPlayer,

        @Column(nullable = false)
        val jerseyNumber: Short,

        @Column(nullable = false)
        val startDate: OffsetDateTime,

        @Column(nullable = true)
        val endDate: OffsetDateTime? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxRosterPlayer

                if (id != other.id) return false
                if (team != other.team) return false
                if (player != other.player) return false
                if (jerseyNumber != other.jerseyNumber) return false
                if (startDate != other.startDate) return false
                if (endDate != other.endDate) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + team.hashCode()
                result = 31 * result + player.hashCode()
                result = 31 * result + jerseyNumber
                result = 31 * result + startDate.hashCode()
                result = 31 * result + (endDate?.hashCode() ?: 0)
                result = 31 * result + owner.hashCode()
                return result
        }

        override fun toString(): String {
                return "PxRosterPlayer(id=$id, team=$team, player=$player, jerseyNumber=$jerseyNumber, startDate=$startDate, endDate=$endDate, owner=$owner)"
        }


}