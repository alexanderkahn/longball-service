package net.alexanderkahn.longball.provider.entity

import java.time.OffsetDateTime
import java.util.*

interface BaseEntity{
    val owner: UserEntity
    val created: OffsetDateTime
    val lastModified: OffsetDateTime
    val id: UUID
}