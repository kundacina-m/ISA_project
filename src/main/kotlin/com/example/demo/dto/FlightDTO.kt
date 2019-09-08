package com.example.demo.dto

import java.util.Date
import java.util.HashSet

data class FlightDTO(

    var id: Int? = null,
    var departureTime: Date? = null,
    var arrivalTime: Date? = null,
    var flightTime: Int = 0,
    var ticketPrice: Double = 0.toDouble(),
    var connectedCities: MutableSet<LocationDTO> = HashSet(),
    var locationDTO: LocationDTO? = null,
    var companyDTO: AvioCompanyDTO? = null,
    var version: Long? = null
)
