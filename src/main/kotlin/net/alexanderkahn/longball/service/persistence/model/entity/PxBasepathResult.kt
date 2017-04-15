package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.PlayLocation
import net.alexanderkahn.longball.service.model.PlayResult
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*

@Entity(name = "basepath_result")
class PxBasepathResult(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_gameplay_event"), nullable = false)
        val gameplayEvent: PxGameplayEvent,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_lineup_player"), nullable = false)
        val lineupPlayer: PxLineupPlayer,

        @Column(nullable = false)
        val location: PlayLocation,

        @Column(nullable = false)
        val playResult: PlayResult,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
):OwnedIdentifiable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as PxBasepathResult

        if (gameplayEvent.id != other.gameplayEvent.id) return false
        if (location != other.location) return false
        if (playResult != other.playResult) return false
        if (id != other.id) return false
        if (owner != other.owner) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = gameplayEvent.id?.hashCode() ?: 1
        result1 = 31 * result1 + location.hashCode()
        result1 = 31 * result1 + playResult.hashCode()
        result1 = 31 * result1 + (id?.hashCode() ?: 0)
        result1 = 31 * result1 + owner.hashCode()
        return result1
    }

    override fun toString(): String {
        return "PxBasepathResult(gameplayEvent=${gameplayEvent.id}, location=$location, basepathResults=$playResult, id=$id, owner=$owner)"
    }
}