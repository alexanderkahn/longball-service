package net.alexanderkahn.longball.provider.entity

import javax.persistence.*

@Entity(name = "team")
data class TeamEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_league"), nullable = false)
        val league: LeagueEntity,

        @Column(nullable = false) val abbreviation: String,
        @Column(nullable = false) val location: String,
        @Column(nullable = false) val nickname: String

) : BaseEntity()