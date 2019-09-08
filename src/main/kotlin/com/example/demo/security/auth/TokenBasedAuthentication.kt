package com.example.demo.security.auth

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails


data class TokenBasedAuthentication(private val principle: UserDetails) : AbstractAuthenticationToken(principle.authorities) {

    var token: String? = null

    override fun isAuthenticated(): Boolean = true
    override fun getCredentials(): Any? = token
    override fun getPrincipal(): UserDetails = principle

    companion object {
        private const val serialVersionUID = 1L
    }

}
