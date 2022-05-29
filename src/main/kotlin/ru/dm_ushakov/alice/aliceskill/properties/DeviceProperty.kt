package ru.dm_ushakov.alice.aliceskill.properties

import com.fasterxml.jackson.databind.JsonNode

interface DeviceProperty {
    val type: String
    val retrievable: Boolean
    val reportable: Boolean

    fun getDescriptionJson(): JsonNode
    fun getStateJson(): JsonNode
}