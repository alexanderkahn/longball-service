package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.UserDTO
import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.longball.provider.repository.UserRepository
import net.alexanderkahn.service.base.api.auth.JwtAuthentication
import net.alexanderkahn.service.base.api.exception.InvalidStateException
import net.alexanderkahn.service.longball.api.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository) : IUserService {
    override fun currentUser(): UserDTO = with(getUserFromContext()) { UserDTO(id, created, lastModified, issuer, username) }
    fun embeddableUser(): UserEntity = getUserFromContext()

    private fun getUserFromContext(): UserEntity {
        val auth = SecurityContextHolder.getContext().authentication as? JwtAuthentication ?: throw InvalidStateException("No current user set")
        val principal = auth.principal
        if (!userRepository.existsByIssuerAndUsername(principal.issuer, principal.username)) {
            userRepository.save(UserEntity(principal.issuer, principal.username))
        }
        return userRepository.findOneByIssuerAndUsername(principal.issuer, principal.username) ?: throw InvalidStateException("Failed to save user. Something went wrong!")
    }
}