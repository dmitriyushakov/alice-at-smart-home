package ru.dm_ushakov.alice.aliceskill.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import ru.dm_ushakov.alice.aliceskill.mvc.ExternalApiHandlerInterceptor

@Configuration
class MVCConfig(
    @Autowired val externalApiHandlerInterceptor: ExternalApiHandlerInterceptor
): WebMvcConfigurationSupport() {
    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(externalApiHandlerInterceptor)
    }
}