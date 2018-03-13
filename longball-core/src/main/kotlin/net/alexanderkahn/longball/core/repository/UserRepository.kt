package net.alexanderkahn.longball.core.repository

import net.alexanderkahn.longball.core.entity.UserEntity
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*


interface UserRepository: PagingAndSortingRepository<UserEntity, UUID> {
    fun existsByIssuerAndUsername(issuer: String, username: String): Boolean
    fun findOneByIssuerAndUsername(issuer: String, username: String): UserEntity?
}