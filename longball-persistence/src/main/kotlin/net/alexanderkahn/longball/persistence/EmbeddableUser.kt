package net.alexanderkahn.longball.persistence


import javax.persistence.Embeddable

@Embeddable
data class EmbeddableUser(val issuer: String, val userId: String)