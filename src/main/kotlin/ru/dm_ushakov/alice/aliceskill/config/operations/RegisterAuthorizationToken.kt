package ru.dm_ushakov.alice.aliceskill.config.operations

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode

class RegisterAuthorizationToken(val userId: String, val authorizationToken: String): ConfigOperation() {
    override fun applyChanges(config: JsonNode) {
        val rootNode = (config as? ObjectNode) ?: error("Expected object as root node!")
        val homesArr = (rootNode["homes"] as? ArrayNode) ?: error("Expected array as homes node!")
        var homeFound = false

        for (childNode in homesArr) {
            val homeNode = (childNode as? ObjectNode) ?: error("Expected object node as home!")
            val userIdNode = homeNode["userId"]
            if (!userIdNode.isTextual) error("Expected userId string in home!")
            val curUserId = userIdNode.asText() as String

            if (userId == curUserId) {
                homeFound = true
                val authorizationKeys = (homeNode["authorizationKeys"] as? ArrayNode) ?: error("Expected array as authorizationKeys node!")
                authorizationKeys.add(authorizationToken)

                break
            }
        }

        if (!homeFound) error("Home with userId \"$userId\" not found!")
    }
}