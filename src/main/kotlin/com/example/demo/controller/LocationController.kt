package com.example.demo.controller


import com.example.demo.dto.LocationDTO
import com.example.demo.models.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.service.LocationService
import com.example.demo.service.UserService

import java.util.ArrayList

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["locations"])
class LocationController {

    @Autowired
    private lateinit var locationService: LocationService

    @Autowired
    internal lateinit var tokenUtils: TokenUtils

    val allLocations: ResponseEntity<List<LocationDTO>>
        @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
        get() {

            val responseLocations = ArrayList<LocationDTO>()

            locationService.findAll().forEach {
                responseLocations.add(it.toDTO())
            }

            return ResponseEntity(responseLocations, HttpStatus.OK)
        }


}
