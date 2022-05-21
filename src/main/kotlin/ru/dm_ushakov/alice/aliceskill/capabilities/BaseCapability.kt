package ru.dm_ushakov.alice.aliceskill.capabilities

abstract class BaseCapability {
    abstract val type: String
    abstract val retrievable: Boolean
    abstract val reportable: Boolean
}