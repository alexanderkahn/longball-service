package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.api.model.ResponseUser

interface IUserService {
    fun currentUser(): ResponseUser
}