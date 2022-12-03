package ru.dm_ushakov.alice.aliceskill.mappings

interface StateMapping<T, C> {
    fun convertFromState(state: T, capability: C): String
    fun convertToState(stateString: String, capability: C): T
}