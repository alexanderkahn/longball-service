package net.alexanderkahn.longball

import net.alexanderkahn.longball.provider.SampleDataLoader
import net.alexanderkahn.service.base.ServiceApplicationBase
import net.alexanderkahn.service.base.api.ITestUserService
import net.alexanderkahn.service.base.api.security.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct

@SpringBootApplication(scanBasePackages = arrayOf("net.alexanderkahn.longball", "net.alexanderkahn.service.base"))
open class LongballServiceApplication: ServiceApplicationBase() {

    @Autowired private lateinit var sampleDataLoader: SampleDataLoader
    @Autowired private lateinit var testUserService: ITestUserService

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            LongballServiceApplication().startService(args)
        }
    }

    @PostConstruct
    fun loadTestData() {
        UserContext.currentUser = testUserService.testUser
        sampleDataLoader.loadSampleData()
    }
}