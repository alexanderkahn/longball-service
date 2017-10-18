package net.alexanderkahn.longball.provider.entity

import net.alexanderkahn.longball.provider.assembler.embeddableUser
import net.alexanderkahn.service.base.api.security.UserContext
import java.util.*
import javax.persistence.Embedded
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity(
        @Embedded val owner: EmbeddableUser = UserContext.embeddableUser,
        @Id val id: UUID = UUID.randomUUID()
)