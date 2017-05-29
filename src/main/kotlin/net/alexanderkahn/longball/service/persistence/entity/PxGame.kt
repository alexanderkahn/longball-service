package net.alexanderkahn.longball.service.persistence.entity

import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import java.time.OffsetDateTime
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "game")
data class PxGame(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_league"), nullable = false) val league: PxLeague,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_away_team"), nullable = false) val awayTeam: PxTeam,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_home_team"), nullable = false) val homeTeam: PxTeam,

        @Column(nullable = false) val startTime: OffsetDateTime,

        @OneToOne(mappedBy = "game", cascade = arrayOf(CascadeType.ALL))
        var result: PxGameResult? = null,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable