package ru.dm_ushakov.alice.aliceskill.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> onceInit() = OnceInitProperty<T>()

class OnceInitProperty<T>: ReadWriteProperty<Any, T> {
    private object NoValue
    private var field: Any? = NoValue

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val fieldValue = field

        if (fieldValue == NoValue) {
            error("Value is not initialized for ${property.name} property!")
        } else {
            return fieldValue as T
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        synchronized(this) {
            if (field == NoValue) {
                field = value
            } else {
                error("Initialization of ${property.name} is allowed only once!")
            }
        }
    }
}