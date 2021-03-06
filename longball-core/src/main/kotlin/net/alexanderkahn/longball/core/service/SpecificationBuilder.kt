package net.alexanderkahn.longball.core.service

import net.alexanderkahn.longball.core.entity.OwnedEntity
import net.alexanderkahn.longball.core.entity.UserEntity
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceFilter
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceSearch
import net.alexanderkahn.service.commons.model.request.parameter.SEARCH_WILDCARD_SPACE
import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

object SpecificationBuilder {
    fun <T : OwnedEntity> build(owner: UserEntity,
                                filterParams: Collection<RequestResourceFilter> = emptySet(),
                                searchParams: RequestResourceSearch? = null
    ): Specification<T> {
        return Specification { root, _, cb ->
            val restrictions = mutableSetOf<Predicate>()
            restrictions.add(cb.equal(root.get<UserEntity>("owner"), owner))
            restrictions.addAll(filterParams.map { root.get<OwnedEntity>(it.filterField).get<UUID>("id").`in`(it.filterTerms) })
            if (searchParams != null) {
                val pattern = "%${searchParams.searchTerm.toLowerCase()}%"
                val concatenatedFields = getConcatenatedFields(cb, root, searchParams.searchFields)
                restrictions.add(cb.like(concatenatedFields, pattern))
            }
            cb.and(*restrictions.toTypedArray())
        }
    }

    private fun getConcatenatedFields(cb: CriteriaBuilder, root: Root<*>, fields: List<String>): Expression<String> {
        var expression: Expression<String> = root.get<String>(fields.get(0))
        for (field: String in fields.drop(1)) {
            expression = if (field == SEARCH_WILDCARD_SPACE) {
                cb.concat(expression, " ")
            } else {
                cb.concat(expression, root.get(field))
            }
        }
        return cb.lower(expression)
    }
}
