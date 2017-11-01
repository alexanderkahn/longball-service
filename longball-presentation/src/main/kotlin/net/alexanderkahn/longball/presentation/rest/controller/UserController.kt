package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.IUserService
import net.alexanderkahn.longball.model.dto.ResponseUser
import net.alexanderkahn.service.base.model.response.ObjectResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(@Autowired private val userService: IUserService) {

    @GetMapping("/user")
    fun getUser(): ObjectResponse<ResponseUser> {
        val currentUser = userService.currentUser()
        return ObjectResponse(currentUser)
    }
}