package ru.dm_ushakov.alice.aliceskill.reflection

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.*
import ru.dm_ushakov.alice.aliceskill.properties.EventProperty
import ru.dm_ushakov.alice.aliceskill.properties.FloatProperty
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

object ComponentsRegistry {
    private val reflections: Reflections get() = ReflectionsHolder.reflections
    private fun <T:Any> findConstructors(tgtClass: KClass<T>): Map<String, KFunction<T>> = reflections.getSubTypesOf(tgtClass.java).mapNotNull {
        val componentName = it.annotations.mapNotNull { annotation -> (annotation as? ComponentName)?.name }.firstOrNull()
        if (componentName == null) {
            null
        } else {
            it.kotlin.primaryConstructor?.let { primaryConstructor -> componentName to primaryConstructor }
        }
    }.toMap()

    val onOffCapConstructors = findConstructors(OnOffCapability::class)
    val toggleCapConstructors = findConstructors(ToggleCapability::class)
    val rangeCapConstructor = findConstructors(RangeCapability::class)
    val colorSettingCapConstructors = findConstructors(ColorSettingCapability::class)
    val modeCapConstructors = findConstructors(ModeCapability::class)
    val eventPropConstructors = findConstructors(EventProperty::class)
    val floatPropConstructors = findConstructors(FloatProperty::class)

    val capConstructors = mapOf(
        "devices.capabilities.on_off" to onOffCapConstructors,
        "devices.capabilities.toggle" to toggleCapConstructors,
        "devices.capabilities.range" to rangeCapConstructor,
        "devices.capabilities.color_setting" to colorSettingCapConstructors,
        "devices.capabilities.mode" to modeCapConstructors
    )

    val propConstructors = mapOf(
        "devices.properties.float" to floatPropConstructors,
        "devices.properties.event" to eventPropConstructors
    )

    private object ReflectionsHolder {
        val reflections = Reflections(ConfigurationBuilder().apply {
            forPackage(".")
        })
    }
}