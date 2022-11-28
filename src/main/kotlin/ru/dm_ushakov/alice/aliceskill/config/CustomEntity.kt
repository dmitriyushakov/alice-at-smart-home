package ru.dm_ushakov.alice.aliceskill.config

import ru.dm_ushakov.alice.aliceskill.devices.Lifecycle

interface CustomEntity: Lifecycle{
    val entityId: String
}