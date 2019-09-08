package com.example.demo.controller


import com.example.demo.dto.AuthorityDTO
import com.example.demo.models.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.demo.service.AuthorityService

import java.util.ArrayList

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["api/authority"])
class AuthorityController {

    @Autowired
    private lateinit var authService: AuthorityService

    val allAuthority: ResponseEntity<List<AuthorityDTO>>
        @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
        get() {
            val authResponse = ArrayList<AuthorityDTO>()

            authService.findAll().forEach {
                authResponse.add(it.toDTO())
            }

            return ResponseEntity(authResponse, HttpStatus.OK)
        }
}
