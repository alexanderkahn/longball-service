package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.model.ResponseUser

interface IUserService {
    fun currentUser(): ResponseUser
}