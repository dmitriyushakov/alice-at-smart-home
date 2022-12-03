package ru.dm_ushakov.alice.aliceskill.config

import com.fasterxml.jackson.annotation.JsonGetter
import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.annotation.EntityType
import ru.dm_ushakov.alice.aliceskill.devices.Lifecycle

interface CustomEntity: Lifecycle{
    val entityId: String

    @get:JsonGetter("component")
    val componentName: String? get() = this::class.annotations.firstNotNullOfOrNull { it as? ComponentName }?.name

    @get:JsonGetter("type")
    val entityType: String? get() = this::class.annotations.firstNotNullOfOrNull { it as? EntityType }?.typeName
}