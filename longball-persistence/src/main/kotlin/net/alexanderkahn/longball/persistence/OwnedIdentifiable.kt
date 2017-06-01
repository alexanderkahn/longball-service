package net.alexanderkahn.longball.persistence

interface OwnedIdentifiable {
    val owner: EmbeddableUser
    val id: Long?
}