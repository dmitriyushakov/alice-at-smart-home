package ru.dm_ushakov.alice.aliceskill.config.operations

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode

class UnlinkAuthorizationToken(val authorizationToken: String): ConfigOperation() {
    override fun applyChanges(config: JsonNode) {
        val rootNode = (config as? ObjectNode) ?: error("Expected object as root node!")
        val homesArr = (rootNode["homes"] as? ArrayNode) ?: error("Expected array as homes node!")
        var tokenFound = false

        for (childNode in homesArr) {
            val homeNode = (childNode as? ObjectNode) ?: error("Expected object node as home!")
            val userIdNode = homeNode["userId"]
            if (!userIdNode.isTextual) error("Expected userId string in home!")

            val authorizationKeys = (homeNode["authorizationKeys"] as? ArrayNode) ?: error("Expected array as authorizationKeys node!")
            for((idx, token) in authorizationKeys.withIndex()) {
                val tokenStr = token.asText() as String
                if (tokenStr == authorizationToken) {
                    authorizationKeys.remove(idx)
                    tokenFound = true
                    break
                }
            }

            if (tokenFound) break
        }

        if (!tokenFound) error("Authorization token \"$authorizationToken\" not found!")
    }
}