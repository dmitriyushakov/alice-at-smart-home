package ru.dm_ushakov.alice.aliceskill.mvc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerInterceptor
import ru.dm_ushakov.alice.aliceskill.annotation.ExternalApi
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KClass

@Component
class ExternalApiHandlerInterceptor(
    @Autowired val internalPort: Int,
    @Autowired val externalApiPort: Int
): HandlerInterceptor {
    private val whiteList: List<KClass<*>> = listOf(BasicErrorController::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod){
            val beanClass = handler.bean::class
            val isExternalApi = beanClass.annotations.any { it is ExternalApi }
            
            val reqPort = request.localPort
            val isInternalPort = reqPort == internalPort
            val isExternalApiPort = reqPort == externalApiPort
            val isInWhiteList = whiteList.any { it === beanClass }

            val allowed = isInWhiteList || (isExternalApi && isExternalApiPort) || ((!isExternalApi) && isInternalPort)

            if (!allowed) {
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
            }
        }

        return super.preHandle(request, response, handler)
    }
}