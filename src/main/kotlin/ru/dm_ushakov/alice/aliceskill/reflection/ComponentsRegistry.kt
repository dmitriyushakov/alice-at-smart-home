package ru.dm_ushakov.alice.aliceskill.reflection

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.annotation.EntityType
import ru.dm_ushakov.alice.aliceskill.capabilities.*
import ru.dm_ushakov.alice.aliceskill.config.CustomEntity
import ru.dm_ushakov.alice.aliceskill.devices.Lifecycle
import ru.dm_ushakov.alice.aliceskill.mappings.*
import ru.dm_ushakov.alice.aliceskill.properties.EventProperty
import ru.dm_ushakov.alice.aliceskill.properties.FloatProperty
import kotlin.reflect.KClass

object ComponentsRegistry {
    private val reflections: Reflections get() = ReflectionsHolder.reflections
    private fun <T:Any> findClasses(tgtClass: KClass<T>): Map<String, Class<out T>> = reflections.getSubTypesOf(tgtClass.java).mapNotNull {
        val componentName = it.annotations.mapNotNull { annotation -> (annotation as? ComponentName)?.name }.firstOrNull()
        if (componentName == null) {
            null
        } else {
            it?.let { cls -> componentName to cls }
        }
    }.toMap()

    val onOffCapClasses = findClasses(OnOffCapability::class)
    val toggleCapClasses = findClasses(ToggleCapability::class)
    val rangeCapClasses = findClasses(RangeCapability::class)
    val colorSettingCapClasses = findClasses(ColorSettingCapability::class)
    val modeCapClasses = findClasses(ModeCapability::class)
    val eventPropClasses = findClasses(EventProperty::class)
    val floatPropClasses = findClasses(FloatProperty::class)

    val customEntityClasses = reflections.getSubTypesOf(CustomEntity::class.java).mapNotNull {
        val componentName = it.annotations.mapNotNull { annotation -> (annotation as? ComponentName)?.name }.firstOrNull()
        if (componentName == null) {
            null
        } else {
            it?.let { cls -> componentName to cls }
        }
    }.groupBy({ it.first }, { it.second }).map { it.key to it.value.mapNotNull { cls ->
        val entityType = cls.annotations.mapNotNull { annotation -> (annotation as? EntityType)?.typeName }.firstOrNull()
        if (entityType == null) {
            null
        } else {
            cls.let { cls -> entityType to cls }
        }
    }.toMap() }.toMap()

    val capClasses = mapOf(
        "devices.capabilities.on_off" to onOffCapClasses,
        "devices.capabilities.toggle" to toggleCapClasses,
        "devices.capabilities.range" to rangeCapClasses,
        "devices.capabilities.color_setting" to colorSettingCapClasses,
        "devices.capabilities.mode" to modeCapClasses
    )

    val propClasses = mapOf(
        "devices.properties.float" to floatPropClasses,
        "devices.properties.event" to eventPropClasses
    )

    val stateMappingImplementations =
        listOf(
            OnOffStateMapping::class, ToggleStateMapping::class, ModeStateMapping::class, ColorStateMapping::class,
            EventPropertyStateMapping::class, FloatPropertyStateMapping::class
        ).associateWith { keyClass ->
            reflections.getSubTypesOf(keyClass.java)
                .mapNotNull { impl ->
                    impl.annotations.firstNotNullOfOrNull { anno -> (anno as? ComponentName)?.name }?.let { comp -> comp to impl }
                }
                .toMap()
        }

    private object ReflectionsHolder {
        val reflections = Reflections(ConfigurationBuilder().apply {
            forPackage(".")
        })
    }
}