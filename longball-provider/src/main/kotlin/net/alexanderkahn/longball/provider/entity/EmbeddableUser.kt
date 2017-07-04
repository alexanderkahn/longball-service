package net.alexanderkahn.longball.provider.entity


@javax.persistence.Embeddable
data class EmbeddableUser(val issuer: String, val userId: String)