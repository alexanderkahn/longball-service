package net.alexanderkahn.longball.core.assembler

import net.alexanderkahn.longball.model.RequestPerson
import net.alexanderkahn.longball.core.entity.PersonEntity
import net.alexanderkahn.longball.core.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PersonAssembler @Autowired constructor(private val userService: UserService) {

    fun toEntity(dto: RequestPerson): PersonEntity {
        return PersonEntity(dto.attributes.first, dto.attributes.last, userService.userEntity())
    }
}