package com.example.demo.security.auth

import java.io.IOException

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter
import com.example.demo.security.TokenUtils

open class TokenAuthenticationFilter(private val tokenUtils: TokenUtils, private val userDetailsService: UserDetailsService) : OncePerRequestFilter() {

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        tokenUtils.getToken(request)?.let { token ->

            tokenUtils.getUsernameFromToken(token)?.let { username ->

                val userDetails = userDetailsService.loadUserByUsername(username)

                if (tokenUtils.validateToken(token, userDetails)!!) {

                    val authentication = TokenBasedAuthentication(userDetails)
                    authentication.token = token
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }

        chain.doFilter(request, response)
    }

}