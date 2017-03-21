package net.alexanderkahn.longball.service

import net.alexanderkahn.base.servicebase.ServiceApplicationBase
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = arrayOf("net.alexanderkahn"))
open class LongballServiceApplication: ServiceApplicationBase(), CommandLineRunner {

    @Autowired private lateinit var testLoader: TestLoader

    private val logger = LoggerFactory.getLogger(LongballServiceApplication::class.java)

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            LongballServiceApplication().startService(args)
        }
    }

    override fun run(vararg args: String?) {
        testLoader.loadData()
    }
}