package ru.dm_ushakov.alice.aliceskill.mappings

interface StateMapping<T, C> {
    fun convertTo(state: T, capability: C): String
    fun convertTo(stateString: String, capability: C): T
}