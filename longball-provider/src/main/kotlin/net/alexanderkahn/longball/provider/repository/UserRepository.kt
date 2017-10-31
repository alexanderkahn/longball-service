package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.UserEntity
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*


interface UserRepository: PagingAndSortingRepository<UserEntity, UUID> {
    fun existsByIssuerAndUsername(issuer: String, username: String): Boolean
    fun findOneByIssuerAndUsername(issuer: String, username: String): UserEntity?
}