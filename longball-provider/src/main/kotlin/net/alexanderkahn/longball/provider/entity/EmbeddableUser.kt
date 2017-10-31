package net.alexanderkahn.longball.provider.entity

import javax.persistence.Embeddable

@Embeddable
data class EmbeddableUser(val issuer: String, val userId: String)