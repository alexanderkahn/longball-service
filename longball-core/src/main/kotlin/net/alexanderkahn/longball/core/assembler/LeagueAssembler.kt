package net.alexanderkahn.longball.core.assembler

import net.alexanderkahn.longball.model.RequestLeague
import net.alexanderkahn.longball.core.entity.LeagueEntity
import net.alexanderkahn.longball.core.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LeagueAssembler @Autowired constructor(private val userService: UserService) {
    fun toEntity(dto: RequestLeague): LeagueEntity {
        return LeagueEntity(dto.attributes.name, userService.userEntity())
    }
}