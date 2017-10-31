package net.alexanderkahn.longball.provider.entity

import java.util.*

interface BaseEntity{
    val owner: UserEntity
    val id: UUID
}