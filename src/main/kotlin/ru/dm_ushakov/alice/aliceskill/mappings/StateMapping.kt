package ru.dm_ushakov.alice.aliceskill.mappings

import com.fasterxml.jackson.annotation.JsonGetter
import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName

interface StateMapping<T, C> {
    @get:JsonGetter("component")
    val componentName: String? get() = this::class.annotations.firstNotNullOfOrNull { it as? ComponentName }?.name
    fun convertFromState(state: T, capability: C): String
    fun convertToState(stateString: String, capability: C): T
}