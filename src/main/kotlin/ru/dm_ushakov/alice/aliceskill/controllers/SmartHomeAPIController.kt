package ru.dm_ushakov.alice.aliceskill.controllers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.dm_ushakov.alice.aliceskill.annotation.ExternalApi
import ru.dm_ushakov.alice.aliceskill.config.ConfigurationLifecycleService
import ru.dm_ushakov.alice.aliceskill.config.ConfigurationService
import ru.dm_ushakov.alice.aliceskill.config.operations.UnlinkAuthorizationToken
import ru.dm_ushakov.alice.aliceskill.util.authorization.cutAuthorizationHeaderValue
import ru.dm_ushakov.alice.aliceskill.util.json.makeJsonObject
import ru.dm_ushakov.alice.aliceskill.util.json.putArray
import ru.dm_ushakov.alice.aliceskill.util.json.putObject

@RestController
@RequestMapping(path = ["/alice/v1.0"])
@ExternalApi
class SmartHomeAPIController(
    @Autowired
    val configurationService: ConfigurationService,
    @Autowired
    val configurationLifecycleService: ConfigurationLifecycleService
) {
    val configuration get() = configurationLifecycleService.configuration
    private fun getHome(authHeader: String) =
        configuration.getHomeByAuthKey(cutAuthorizationHeaderValue(authHeader))

    @RequestMapping(method = [RequestMethod.HEAD], path = [""])
    fun check() {

    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/user/unlink"])
    fun unlinkUser(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorizationHeader: String,
        @RequestHeader("X-Request-Id")  reqId: String
    ) {
        configurationService.applyChanges(UnlinkAuthorizationToken(cutAuthorizationHeaderValue(authorizationHeader)))
        configurationLifecycleService.update()
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/user/devices"])
    fun getDescriptionInfo(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorizationHeader: String,
        @RequestHeader("X-Request-Id")  reqId: String
    ): JsonNode {
        val user = getHome(authorizationHeader)

        return makeJsonObject {
            put("request_id", reqId)
            replace("payload", user.getDescriptionJson())
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/user/devices/query"])
    fun getStateInfo(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorizationHeader: String,
        @RequestHeader("X-Request-Id") reqId: String,
        @RequestBody requestBody: ObjectNode
    ): JsonNode {
        val user = getHome(authorizationHeader)
        val idToDevice = user.devices.associateBy { it.id }
        val devicesArr = requestBody["devices"] as? ArrayNode ?: error("\"devices\" field required!")
        val devices = devicesArr.map { it["id"].asText() as String }

        return makeJsonObject {
            put("request_id", reqId)
            putObject("payload") {
                putArray("devices") {
                    for (deviceId in devices) {
                        val device = idToDevice[deviceId] ?: error("Device $deviceId not found!")
                        add(device.getStateJson())
                    }
                }
            }
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/user/devices/action"])
    fun changeCapabilityState(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorizationHeader: String,
        @RequestHeader("X-Request-Id")  reqId: String,
        @RequestBody changeStateRequest: ObjectNode
    ): JsonNode {
        val user = getHome(authorizationHeader)

        return makeJsonObject {
            put("request_id", reqId)
            replace("payload", user.changeCapabilityState(changeStateRequest["payload"]))
        }
    }
}