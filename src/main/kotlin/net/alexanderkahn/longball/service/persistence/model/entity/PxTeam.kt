package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "team")
class PxTeam(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Column(nullable = false)
        val abbreviation: String,

        @Column(nullable = false)
        val location: String, val nickname: String,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxTeam

                if (id != other.id) return false
                if (abbreviation != other.abbreviation) return false
                if (location != other.location) return false
                if (nickname != other.nickname) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + abbreviation.hashCode()
                result = 31 * result + location.hashCode()
                result = 31 * result + nickname.hashCode()
                result = 31 * result + owner.hashCode()
                return result
        }

        override fun toString(): String {
                return "PxTeam(id=$id, abbreviation='$abbreviation', location='$location', nickname='$nickname', owner=$owner)"
        }


}