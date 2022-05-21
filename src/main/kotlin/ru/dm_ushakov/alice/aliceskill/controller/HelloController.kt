package ru.dm_ushakov.alice.aliceskill.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.dm_ushakov.alice.aliceskill.model.Greetings

@RestController
class HelloController {
    @GetMapping("/hello")
    fun hello() = Greetings("Hello", "World")
}