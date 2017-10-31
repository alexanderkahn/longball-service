package net.alexanderkahn.longball.provider.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "longball_user")
data class UserEntity(
        @Column(nullable = false) val issuer: String,
        @Column(nullable = false) val username: String,
        @Id val id: UUID = UUID.randomUUID()
)
