package com.kotlinspring.controller

import com.kotlinspring.service.GreetingsService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/greetings")
class GreetingsController(val greetingsService: GreetingsService) {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/{name}")
    fun requestGreetings(@PathVariable("name") name: String): String {
        logger.info { "Name is: $name" }
        return greetingsService.retrieveGreeting(name)
    }
}