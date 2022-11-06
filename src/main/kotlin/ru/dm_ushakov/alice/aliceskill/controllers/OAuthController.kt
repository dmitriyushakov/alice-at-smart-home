package ru.dm_ushakov.alice.aliceskill.controllers

import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import ru.dm_ushakov.alice.aliceskill.annotation.ExternalApi
import ru.dm_ushakov.alice.aliceskill.authorization.AuthorizationService
import ru.dm_ushakov.alice.aliceskill.util.json.makeJsonObject

@RestController
@ExternalApi
@RequestMapping(path=["/oauth"])
class OAuthController(
    @Autowired val authorizationService: AuthorizationService
) {
    private fun getResource(name: String): ByteArray = OAuthController::class.java.classLoader.getResourceAsStream(name).readBytes()

    @RequestMapping(method=[RequestMethod.GET], value=["/authorize"], produces = ["text/html"])
    fun getAuthorizePage() = getResource("oauth_page.html")

    @RequestMapping(method=[RequestMethod.GET], value=["/generate_code"])
    fun generateCode(@RequestParam("client_id") clientId: String): SseEmitter {
        val emitter = SseEmitter()

        val request = authorizationService.generateCode(clientId) { code ->
            try {
                SseEmitter.event()
                    .data(code)
                    .let { ev -> emitter.send(ev) }

                emitter.complete()
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw ex
            }
        }

        emitter.onCompletion {
            request.cancel()
        }

        return emitter
    }

    @RequestMapping(method=[RequestMethod.GET, RequestMethod.POST], value=["/get_token"])
    fun getToken(
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("code") code: String): ObjectNode {
        val token = authorizationService.registerCode(clientId, clientSecret, code)

        return makeJsonObject {
            put("access_token", token)
            put("token_type", "bearer")
            put("expires_in", 4_294_967_296L)
        }
    }
}