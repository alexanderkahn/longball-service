package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.UserDTO
import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.service.base.api.auth.JwtAuthentication
import net.alexanderkahn.service.base.api.exception.InvalidStateException
import net.alexanderkahn.service.longball.api.IUserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService : IUserService {
    override fun currentUser(): UserDTO = getUserFromContext()
    fun embeddableUser(): EmbeddableUser = with(getUserFromContext()) { EmbeddableUser(issuer, username) }

    private fun getUserFromContext(): UserDTO {
        val auth = SecurityContextHolder.getContext().authentication as? JwtAuthentication ?: throw InvalidStateException("No current user set")
        return with(auth.principal) { UserDTO(UUID.randomUUID(), issuer, username) }

    }
}