package ru.dm_ushakov.alice.aliceskill.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.dm_ushakov.alice.aliceskill.authorization.AuthorizationService

@RestController
@RequestMapping(path = ["/internal_api"])
class AuthorizationController(
    @Autowired val authorizationService: AuthorizationService
) {
    @RequestMapping(path = ["/join"], method = [RequestMethod.GET])
    fun joinToHome(@RequestParam("user_id") userId: String) =
        authorizationService.joinRequest(userId)
}