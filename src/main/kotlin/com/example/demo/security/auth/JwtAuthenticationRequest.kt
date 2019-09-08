package com.example.demo.security.auth

data class JwtAuthenticationRequest(

    var username: String? = null,
    var password: String? = null
)
