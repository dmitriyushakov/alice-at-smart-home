package ru.dm_ushakov.alice.aliceskill.capabilities.model

data class TemperatureRange(val min: Int? = null, val max: Int? = null) {
    init {
        if (min != null) {
            if (min < 0) error("Min value can't be less than zero!")
        }
        if (max != null) {
            if (max < 0) error("Max value can't be less than zero!")

            if (min != null) {
                if (min > max) error("Min value can't be larger than zero!")
            }
        }
    }
}