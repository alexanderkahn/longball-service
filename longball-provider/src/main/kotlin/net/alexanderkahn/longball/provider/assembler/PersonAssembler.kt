package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.RequestPerson
import net.alexanderkahn.longball.provider.entity.PersonEntity
import net.alexanderkahn.longball.provider.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PersonAssembler @Autowired constructor(private val userService: UserService) {

    fun toEntity(dto: RequestPerson): PersonEntity {
        return PersonEntity(dto.attributes.first, dto.attributes.last, userService.embeddableUser())
    }
}