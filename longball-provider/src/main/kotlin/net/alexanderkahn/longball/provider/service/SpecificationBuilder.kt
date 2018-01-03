package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.provider.entity.BaseEntity
import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.service.base.model.request.RequestResourceSearch
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root

object SpecificationBuilder {
    fun <T : BaseEntity> matchSearch(owner: UserEntity, searchParams: RequestResourceSearch): Specification<T> {
        val pattern = "%${searchParams.searchTerm.replace(" ", "").toLowerCase()}%"
        return Specification {
            root, _, cb -> val concatenatedFields = getConcatenatedFields(cb, root, searchParams.searchFields)
            cb.and(cb.equal(root.get<UserEntity>("owner"), owner), cb.like(concatenatedFields, pattern))
        }
    }

    private fun getConcatenatedFields(cb: CriteriaBuilder, root: Root<*>, fields: List<String>): Expression<String> {
        var expression: Expression<String> = root.get<String>(fields.get(0))
        for (field: String in fields.drop(1)) {
            expression = cb.concat(expression, root.get(field))
        }
        return cb.lower(expression)
    }
}
