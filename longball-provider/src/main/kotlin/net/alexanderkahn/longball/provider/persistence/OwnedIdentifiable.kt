package net.alexanderkahn.longball.provider.persistence

interface OwnedIdentifiable {
    val owner: EmbeddableUser
    val id: Long?
}