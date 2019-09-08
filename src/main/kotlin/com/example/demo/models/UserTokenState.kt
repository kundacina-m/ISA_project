package com.example.demo.models

data class UserTokenState(
    var accessToken: String? = null,
    var expiresIn: Long? = null
)