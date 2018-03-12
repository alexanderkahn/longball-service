package net.alexanderkahn.longball.provider.test

import net.alexanderkahn.longball.provider.entity.*
import java.time.LocalDateTime
import java.time.OffsetDateTime

object TestSubjects {
    val owner = UserEntity("Issuer", "Username", "Display name", OffsetDateTime.now())
    val theShow = LeagueEntity("The Show", owner)
    val raccoons = TeamEntity(theShow, "PDX", "Portland", "Raccoons", owner)
    val rangerRick = PersonEntity("Ranger", "Rick", owner)
    val rickPosition = RosterPositionEntity(raccoons, rangerRick, 17, LocalDateTime.now(), null, owner)
}