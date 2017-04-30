package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.PlayLocation
import net.alexanderkahn.longball.service.model.PlayResult
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*

@Entity(name = "basepath_result")
data class PxBasepathResult(

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
):OwnedIdentifiable