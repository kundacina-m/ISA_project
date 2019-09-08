package com.example.demo.dto

data class FriendshipDTO(
    var sender: UserDTO? = null,
    var receiver: UserDTO? = null,
    var isValid: Boolean = false,
    var version: Long? = null
)
