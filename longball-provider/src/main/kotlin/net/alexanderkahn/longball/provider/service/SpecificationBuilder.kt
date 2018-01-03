package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.UserEntity
import org.springframework.data.jpa.domain.Specification

object SpecificationBuilder {
    fun leagueNameMatches(owner: UserEntity, partialText: String): Specification<LeagueEntity> {
        val pattern = "%$partialText%".toLowerCase()
        return Specification {
            root, _, cb -> cb.and(cb.equal(root.get<UserEntity>("owner"), owner), cb.like(cb.lower(root.get("name")), pattern))
        }
    }
}
