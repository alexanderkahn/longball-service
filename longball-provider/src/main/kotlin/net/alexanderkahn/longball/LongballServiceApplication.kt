package net.alexanderkahn.longball

import net.alexanderkahn.longball.presentation.rest.TestLoader
import net.alexanderkahn.servicebase.provider.ServiceApplicationBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = arrayOf("net.alexanderkahn.longball", "net.alexanderkahn.servicebase"))
open class LongballServiceApplication: ServiceApplicationBase(), CommandLineRunner {

    //TODO: this is the only thing that ties provider to core. Put a hook to this into presentation and get rid of the dependency.
    @Autowired private lateinit var testLoader: TestLoader

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            LongballServiceApplication().startService(args)
        }
    }

    override fun run(vararg args: String?) {
        testLoader.loadWithUser()
    }
}