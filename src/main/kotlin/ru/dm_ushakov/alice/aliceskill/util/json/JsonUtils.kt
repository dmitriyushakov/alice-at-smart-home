package ru.dm_ushakov.alice.aliceskill.util.json

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode

fun makeJsonObject(builder: ObjectNode.() -> Unit): ObjectNode = JsonNodeFactory.instance.objectNode().apply(builder)

fun ObjectNode.putObject(objectName: String, builder: ObjectNode.() -> Unit) {
    putObject(objectName).apply(builder)
}

fun ObjectNode.putArray(arrayName: String, builder: ArrayNode.() -> Unit) {
    putArray(arrayName).apply(builder)
}

fun ArrayNode.addObject(builder: ObjectNode.() -> Unit) {
    addObject().apply(builder)
}