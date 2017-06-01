package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.core.TestLoaderService
import net.alexanderkahn.servicebase.core.security.UserContext
import net.alexanderkahn.servicebase.model.User
import net.alexanderkahn.servicebase.presentation.security.jwt.jwtTestUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestLoader(@Autowired private val testLoaderService: TestLoaderService) {
    fun loadWithUser(user: User = jwtTestUser()) {
        UserContext.currentUser = user
        testLoaderService.loadData()
    }
}