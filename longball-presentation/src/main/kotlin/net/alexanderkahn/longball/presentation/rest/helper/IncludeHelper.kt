package net.alexanderkahn.longball.presentation.rest.helper

import net.alexanderkahn.longball.api.model.ModelTypes
import net.alexanderkahn.longball.api.service.IPersonService
import net.alexanderkahn.service.commons.model.response.body.data.ModifiableResourceObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class IncludeHelper(@Autowired private val personService: IPersonService) {

    fun include(typeToIdsMap: Map<String, List<UUID>>): List<ModifiableResourceObject> {
        return typeToIdsMap.flatMap { (type, ids) -> getResourceObjectsForType(type, ids.distinct()) }.sortedWith(compareBy({it.type}, {it.meta.created}))
    }

    private fun getResourceObjectsForType(type: String, ids: List<UUID>): List<ModifiableResourceObject> {
        return when (type) {
            ModelTypes.PEOPLE -> ids.map { personService.get(it) }
            else -> listOf()
        }
    }
}