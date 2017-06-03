package net.alexanderkahn.longball.core.persistence

interface OwnedIdentifiable {
    val owner: EmbeddableUser
    val id: Long?
}