package ru.dm_ushakov.alice.aliceskill.config

abstract class BasicCustomEntity(
    override val entityId: String): CustomEntity {
    override val componentName by lazy { super.componentName }
    override val entityType by lazy { super.entityType }
}