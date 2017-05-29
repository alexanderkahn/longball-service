package net.alexanderkahn.longball.service

import net.alexanderkahn.servicebase.provider.ServiceApplicationBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = arrayOf("net.alexanderkahn"))
open class LongballServiceApplication: ServiceApplicationBase(), CommandLineRunner {

    @Autowired private lateinit var testLoader: TestLoader

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            LongballServiceApplication().startService(args)
        }
    }

    override fun run(vararg args: String?) {
        testLoader.loadData()
    }
}