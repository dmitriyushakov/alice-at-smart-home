package ru.dm_ushakov.alice.aliceskill.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Service
class BasicConfigurationLifecycleService(
    @Autowired
    private val configurationService: ConfigurationService
    ): ConfigurationLifecycleService {
    private var loadedConf: SkillConfiguration = configurationService.getConfiguration()
    private val lock: Lock = ReentrantLock()

    init {
        callOnCreate()
    }

    override val configuration: SkillConfiguration
        get() = loadedConf

    private fun callOnCreate() {
        for((_, home) in loadedConf.homes) {
            home.onCreate()
        }
    }

    private fun callOnDestroy() {
        for((_, home) in loadedConf.homes) {
            home.onDestroy()
        }
    }

    override fun update() {
        lock.withLock {
            val newConf = configurationService.getConfiguration()
            callOnDestroy()
            loadedConf = newConf
            callOnCreate()
        }
    }
}