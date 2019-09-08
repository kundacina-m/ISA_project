package com.example.demo.security.auth

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

@Component
open class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class)

    override fun commence(httpServletRequest: HttpServletRequest,
                          httpServletResponse: HttpServletResponse, e: AuthenticationException) {

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error occurred")
    }
}
