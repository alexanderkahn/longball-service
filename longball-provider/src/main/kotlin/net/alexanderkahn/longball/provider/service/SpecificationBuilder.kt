package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.provider.entity.BaseEntity
import net.alexanderkahn.longball.provider.entity.UserEntity
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
    fun <T : BaseEntity> build(owner: UserEntity, filterParams: Collection<RequestResourceFilter>, searchParams: RequestResourceSearch?): Specification<T> {
        return Specification { root, _, cb ->
            val restrictions = mutableSetOf<Predicate>()
            restrictions.add(cb.equal(root.get<UserEntity>("owner"), owner))
            restrictions.addAll(filterParams.map { root.get<BaseEntity>(it.filterField).get<UUID>("id").`in`(it.filterTerms) })
            if (searchParams != null) {
                val pattern = "%${searchParams.searchTerm.toLowerCase()}%"
                val concatenatedFields = getConcatenatedFields(cb, root, searchParams.searchFields)
                restrictions.add(cb.like(concatenatedFields, pattern))
            }
            cb.and(*restrictions.toTypedArray())
        }
    }

    //TODO: would be fun to allow for case sensitivity. Lots of stuff you could do here to make search more robust.
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
