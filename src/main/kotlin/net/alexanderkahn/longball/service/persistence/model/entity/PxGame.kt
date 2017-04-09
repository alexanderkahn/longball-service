package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import java.time.OffsetDateTime
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "game")
class PxGame(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_league"), nullable = false)
        val league: PxLeague,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_away_team"), nullable = false)
        val awayTeam: PxTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_home_team"), nullable = false)
        val homeTeam: PxTeam,

        @Column(nullable = false)
        val startTime: OffsetDateTime,

        @OneToMany(mappedBy = "game", cascade = arrayOf(CascadeType.ALL))
        @OrderBy("id ASC")
        var innings: MutableList<PxInning> = mutableListOf(),

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxGame

                if (id != other.id) return false
                if (league != other.league) return false
                if (awayTeam != other.awayTeam) return false
                if (homeTeam != other.homeTeam) return false
                if (startTime != other.startTime) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + league.hashCode()
                result = 31 * result + awayTeam.hashCode()
                result = 31 * result + homeTeam.hashCode()
                result = 31 * result + startTime.hashCode()
                result = 31 * result + owner.hashCode()
                return result
        }

        override fun toString(): String {
                return "PxGame(id=$id, league=$league, awayTeam=$awayTeam, homeTeam=$homeTeam, startTime=$startTime, owner=$owner)"
        }
}