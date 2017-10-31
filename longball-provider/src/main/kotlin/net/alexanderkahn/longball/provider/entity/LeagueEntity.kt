package net.alexanderkahn.longball.provider.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "league")
class LeagueEntity(
        @Column(nullable = false) val name: String,
        @Embedded override val owner: EmbeddableUser,
        @Id override val id: UUID = UUID.randomUUID()
) : BaseEntity