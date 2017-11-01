package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.RequestLeague
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LeagueAssembler @Autowired constructor(private val userService: UserService) {
    fun toEntity(dto: RequestLeague): LeagueEntity {
        return LeagueEntity(dto.attributes.name, userService.embeddableUser())
    }
}