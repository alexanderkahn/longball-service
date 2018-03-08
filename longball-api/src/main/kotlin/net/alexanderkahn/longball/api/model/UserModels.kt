package net.alexanderkahn.longball.api.model

import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*

data class ResponseUser(
    override val id: UUID,
    override val meta: ModifiableResourceMeta,
    override val attributes: UserAttributes
) : ResourceObject {
    override val type = "users"
    override val relationships = null
}

data class UserAttributes(val issuer: String, val username: String, val displayName: String)