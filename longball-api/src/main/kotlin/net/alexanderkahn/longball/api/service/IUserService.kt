package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.model.dto.ResponseUser

interface IUserService {
    fun currentUser(): ResponseUser
}