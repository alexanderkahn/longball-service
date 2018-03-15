package net.alexanderkahn.longball.core.entity

import net.alexanderkahn.longball.core.getEntityClasses
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.persistence.EntityListeners

class EntityListenerReflectionTest {

    private val expectedListeners = listOf(UpdateLastModifiedListener::class)

    @Test
    fun entitiesHaveExpectedListeners() {
        val classesToScan = getEntityClasses()
        val validationFailures = mutableListOf<String>()

        classesToScan.forEach { entityClazz ->
            val listener = entityClazz.getAnnotation(EntityListeners::class.java)
            if (listener == null) {
                validationFailures.add("Did not find expected annotation ${EntityListeners::class.simpleName} on entity $entityClazz")
            } else {
                expectedListeners.forEach {
                    if (!listener.value.contains(it)) {
                        validationFailures.add("Class $entityClazz EntityListeners did not include expected listener $it")
                    }
                }
            }
        }

        if (validationFailures.isNotEmpty()) {
            Assertions.fail<String>("Found entities of missing expected listeners: ${validationFailures.joinToString()}")
        }
    }
}