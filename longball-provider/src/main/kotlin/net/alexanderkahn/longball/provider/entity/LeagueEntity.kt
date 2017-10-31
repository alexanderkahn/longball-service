package net.alexanderkahn.longball.provider.entity

import java.util.*
import javax.persistence.*

@Entity(name = "league")
class LeagueEntity(
        @Column(nullable = false) val name: String,
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,
        @Id override val id: UUID = UUID.randomUUID()
) : BaseEntity