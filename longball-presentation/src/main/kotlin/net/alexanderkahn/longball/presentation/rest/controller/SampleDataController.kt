package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.service.longball.api.ISampleDataLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sampledata")
class SampleDataController(@Autowired private val sampleDataLoader: ISampleDataLoader) {

    @PutMapping
    fun putSampleData() {
        sampleDataLoader.loadSampleData()
    }
}