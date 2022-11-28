package ru.dm_ushakov.alice.aliceskill.config.operations

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode

class CreateOrUpdateCustomEntity(val userId: String, val entityData: JsonNode): ConfigOperation() {
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
                var entityFound = false
                val entityId = entityData["entityId"].asText() ?: error("Expected \"entityId\" key in entity!")
                val customEntitiesNode = (homeNode["customEntities"] as? ArrayNode) ?: error("Expected \"customEntities\" as array node!")

                for ((idx,childEntityNode) in customEntitiesNode.withIndex()) {
                    val childEntityId = childEntityNode["entityId"].asText() ?: error("Expected \"entityId\" key in entity!")
                    if (entityId == childEntityId) {
                        entityFound = true
                        customEntitiesNode[idx] = entityData
                        break
                    }
                }

                if (!entityFound) {
                    customEntitiesNode.add(entityData)
                }

                break
            }
        }

        if (!homeFound) error("Home with userId \"$userId\" not found!")
    }
}