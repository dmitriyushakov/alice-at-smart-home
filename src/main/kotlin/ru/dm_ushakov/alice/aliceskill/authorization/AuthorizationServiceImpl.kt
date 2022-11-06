package ru.dm_ushakov.alice.aliceskill.authorization

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.dm_ushakov.alice.aliceskill.config.ConfigurationLifecycleService
import ru.dm_ushakov.alice.aliceskill.config.ConfigurationService
import ru.dm_ushakov.alice.aliceskill.config.operations.RegisterAuthorizationToken
import ru.dm_ushakov.alice.aliceskill.util.authorization.generateRandomCode
import ru.dm_ushakov.alice.aliceskill.util.authorization.generateRandomToken
import ru.dm_ushakov.alice.aliceskill.util.unauthorized
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.random.Random

@Service
class AuthorizationServiceImpl(
    @Autowired val clientId: String?,
    @Autowired val clientSecret: String?,
    @Autowired val configurationService: ConfigurationService,
    @Autowired val configurationLifecycleService: ConfigurationLifecycleService
): AuthorizationService {
    private data class AuthInfo(val userId: String, val authToken: String)
    private val lock: Lock = ReentrantLock()
    private val generateCodeRequests: MutableSet<GenerateCodeRequestImpl> = mutableSetOf()
    private val codeToAuth: MutableMap<String, AuthInfo> = mutableMapOf()

    private fun applyConfigurationOperation(operation: (JsonNode) -> Unit) {
        configurationService.applyChanges(operation)
        configurationLifecycleService.update()
    }

    private fun validateClientId(receivedClientId: String) {
        val ourClientId = clientId
        if (ourClientId != null) {
            if (ourClientId != receivedClientId) {
                unauthorized("Received wrong client id!")
            }
        }
    }

    private fun validateClientSecret(receivedClientSecret: String) {
        val ourClientSecret = clientSecret
        if (ourClientSecret != null) {
            if (ourClientSecret != receivedClientSecret) {
                unauthorized("Received wrong client secret!")
            }
        }
    }

    private class GenerateCodeRequestImpl(
        private val service: AuthorizationServiceImpl,
        private val callback: (String) -> Unit): GenerateCodeRequest {
        private val hash = Random.Default.nextInt()

        override fun cancel() {
            service.cancel(this)
        }

        fun putCode(code: String) {
            callback(code)
        }

        override fun hashCode() = hash
        override fun equals(other: Any?) = this === other
    }

    private fun cancel(request: GenerateCodeRequestImpl) {
        lock.withLock {
            generateCodeRequests.remove(request)
        }
    }

    override fun joinRequest(userId: String): JoinRequestResult {
        var authorizedNumber = 0
        lock.withLock {
            for (req in generateCodeRequests) {
                authorizedNumber++
                val code = generateRandomCode()
                val token = generateRandomToken()
                val auth = AuthInfo(userId, token)
                codeToAuth[code] = auth

                req.putCode(code)
            }

            generateCodeRequests.clear()
        }

        return JoinRequestResult(authorizedNumber > 0, authorizedNumber)
    }

    override fun generateCode(clientId: String, callback: (String) -> Unit): GenerateCodeRequest {
        validateClientId(clientId)

        val req = GenerateCodeRequestImpl(this, callback)
        lock.withLock {
            generateCodeRequests.add(req)
        }

        return req
    }

    override fun registerCode(clientId: String, clientSecret: String, code: String): String {
        validateClientId(clientId)
        validateClientSecret(clientSecret)

        val authInfo = lock.withLock { codeToAuth.remove(code) } ?: unauthorized("Received unknown code!")
        applyConfigurationOperation(RegisterAuthorizationToken(authInfo.userId, authInfo.authToken))
        return authInfo.authToken
    }
}