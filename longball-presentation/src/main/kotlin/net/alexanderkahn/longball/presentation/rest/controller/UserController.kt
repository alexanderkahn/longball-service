package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.model.dto.UserDTO
import net.alexanderkahn.service.base.presentation.response.ObjectResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.longball.api.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(@Autowired private val userService: IUserService) {

    @GetMapping("/user")
    fun getUser(): ObjectResponse<ResponseUser> {
        val currentUser = userService.currentUser()
        return ObjectResponse(ResponseUser(currentUser))
    }

    class ResponseUser(userDTO: UserDTO) : ResponseResourceObject {
        override val type = "users"
        override val id = userDTO.id
        override val attributes = UserAttributes(userDTO.issuer, userDTO.username)
        override val relationships = null
    }

    class UserAttributes(val issuer: String, val username: String)
}