package net.alexanderkahn.longball.core.entity

import net.alexanderkahn.longball.core.getEntityClasses
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EntityInheritanceReflectionTest {

    @Test
    internal fun allEntitiesImplementBaseEntity() {
        val classesToScan = getEntityClasses()
        val validationFailures = mutableListOf<String>()

        classesToScan.forEach { entityClazz ->
            if (!BaseEntity::class.java.isAssignableFrom(entityClazz)) {
                validationFailures.add("Class $entityClazz was expected to implement BaseEntity, but did not")
            }
        }

        if (validationFailures.isNotEmpty()) {
            Assertions.fail<String>("Errors: ${validationFailures.joinToString()}")
        }
    }

    @Test
    internal fun entitiesImplementOwnedEntity() {
        val exceptedClasses: List<Class<*>> = listOf(UserEntity::class.java)
        val classesToScan = getEntityClasses()
        val validationFailures = mutableListOf<String>()

        classesToScan.filter { !exceptedClasses.contains(it) }.forEach { entityClazz ->
            if (!OwnedEntity::class.java.isAssignableFrom(entityClazz)) {
                validationFailures.add("Class $entityClazz was expected to implement OwnedEntity, but did not")
            }
        }

        if (validationFailures.isNotEmpty()) {
            Assertions.fail<String>("Errors: ${validationFailures.joinToString()}")
        }
    }
}