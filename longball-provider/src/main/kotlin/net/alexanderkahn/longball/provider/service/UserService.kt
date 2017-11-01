package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.service.IUserService
import net.alexanderkahn.longball.model.dto.ResponseUser
import net.alexanderkahn.longball.model.dto.UserAttributes
import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.longball.provider.repository.UserRepository
import net.alexanderkahn.service.base.api.auth.JwtAuthentication
import net.alexanderkahn.service.base.model.exception.InvalidStateException
import net.alexanderkahn.service.base.model.response.body.meta.ModifiableResourceMeta
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository) : IUserService {
    override fun currentUser(): ResponseUser = with(getUserFromContext()) {
        ResponseUser(id, ModifiableResourceMeta(created, lastModified), UserAttributes(issuer, username)) }
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