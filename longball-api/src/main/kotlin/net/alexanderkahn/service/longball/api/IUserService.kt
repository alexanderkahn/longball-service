package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.dto.UserDTO

interface IUserService {
    fun currentUser(): UserDTO
}