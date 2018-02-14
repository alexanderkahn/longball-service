package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.service.IUserService
import net.alexanderkahn.longball.model.dto.ResponseUser
import net.alexanderkahn.longball.model.dto.UserAttributes
import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.longball.provider.repository.UserRepository
import net.alexanderkahn.service.commons.firebaseauth.jws.JwsAuthentication
import net.alexanderkahn.service.commons.model.exception.InvalidStateException
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository) : IUserService {
    override fun currentUser(): ResponseUser = with(getUserFromContext()) {
        ResponseUser(id, ModifiableResourceMeta(created, lastModified), UserAttributes(issuer, username, displayName)) }

    fun userEntity(): UserEntity = getUserFromContext()

    private fun getUserFromContext(): UserEntity {
        val auth = SecurityContextHolder.getContext().authentication as? JwsAuthentication ?: throw InvalidStateException("No current user set")
        val principal = auth.principal
        var userEntity = userRepository.findOneByIssuerAndUsername(principal.issuer, principal.username)
        if (userEntity == null) {
            userEntity = UserEntity(principal.issuer, principal.username, auth.details.displayName)
            userRepository.save(userEntity)
        } else if (userEntity.displayName != auth.details.displayName) {
            userEntity.displayName = auth.details.displayName
            userRepository.save(userEntity)
        }
        return userEntity
    }
}