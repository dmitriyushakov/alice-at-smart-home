package ru.dm_ushakov.alice.aliceskill.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.dm_ushakov.alice.aliceskill.annotation.ExternalApi
import ru.dm_ushakov.alice.aliceskill.model.Greetings

@ExternalApi
@RestController
class ExternalHelloController {
    @GetMapping("/externalHello")
    fun hello() = Greetings("Hello", "External World")
}