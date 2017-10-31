package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.PersonDTO
import net.alexanderkahn.longball.provider.entity.PersonEntity
import net.alexanderkahn.longball.provider.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PersonAssembler @Autowired constructor(private val userService: UserService) {

    fun toEntity(dto: PersonDTO): PersonEntity {
        return PersonEntity(dto.first, dto.last, userService.embeddableUser())
    }
}