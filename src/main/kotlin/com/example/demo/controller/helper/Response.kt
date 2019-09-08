package com.example.demo.controller.helper

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


object Response {

    fun <T> unauthorized(): ResponseEntity<T> = ResponseEntity(HttpStatus.UNAUTHORIZED)
    fun <T> notFound(): ResponseEntity<T> = ResponseEntity(HttpStatus.NOT_FOUND)
    fun <T> ok(): ResponseEntity<T> = ResponseEntity(HttpStatus.OK)
    fun <T> created(): ResponseEntity<T> = ResponseEntity(HttpStatus.CREATED)
}