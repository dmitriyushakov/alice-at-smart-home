package ru.dm_ushakov.alice.aliceskill.config

import org.apache.catalina.connector.Connector
import org.apache.coyote.http11.Http11NioProtocol
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.security.KeyStore

/*
Command to generate certificate:

keytool -genkey -keyalg RSA -alias external_api -keystore external_api_keystore.jks -storepass alice_pass -validity 720 -keysize 2048
 */

@Configuration
open class ServletConfiguration(
    @Value("\${server.port:8080}") private val serverPort: Int,
    @Value("\${server.externalApiKeypass:alice_pass}") private val keypass: String,
    @Value("\${server.externalApiKeystore:external_api_keystore.jks}") private val keystorePath: String,
    @Value("\${server.externalApiPortEnabled:true}") private val externalApiInboundPortEnabled: Boolean,
    @Value("\${server.externalApiPort:null}") private val externalApiInboundPortProperty: String,
    @Value("\${server.externalApiPortSecured:true}") private val externalApiInboundPortSecured: Boolean
    ) {
    private val externalApiInboundPort: Int by lazy {
        externalApiInboundPortProperty.let { if(it=="null") null else it.toInt() } ?:
        (if (externalApiInboundPortSecured) 9443 else 9000)
    }

    @Bean
    fun internalPort(): Int = serverPort

    @Bean
    fun externalApiPort(): Int = if (externalApiInboundPortEnabled) externalApiInboundPort else serverPort

    @Bean
    fun servletContainer(): ServletWebServerFactory {
        val tomcat = TomcatServletWebServerFactory()

        if (externalApiInboundPortEnabled) {
            tomcat.addAdditionalTomcatConnectors(externalApiInboundConnector())
        }

        return tomcat
    }

    private fun externalApiInboundConnector(): Connector {
        return if (externalApiInboundPortSecured) {
            externalApiInboundConnectorSecured()
        } else {
            externalApiInboundConnectorOpen()
        }
    }

    private fun externalApiInboundConnectorOpen(): Connector {
        val connector = Connector("org.apache.coyote.http11.Http11NioProtocol")

        connector.apply {
            scheme = "http"
            secure = false
            port = externalApiInboundPort
        }

        return connector
    }

    private fun externalApiInboundConnectorSecured(): Connector {
        val connector = Connector("org.apache.coyote.http11.Http11NioProtocol")
        val protocol = connector.protocolHandler as Http11NioProtocol

        connector.apply {
            scheme = "https"
            secure = true
            port = externalApiInboundPort
        }

        protocol.apply {
            isSSLEnabled = true
            keystoreFile = keystorePath
            keystorePass = keypass
            keyAlias = keystoreAlias
        }

        return connector
    }
}