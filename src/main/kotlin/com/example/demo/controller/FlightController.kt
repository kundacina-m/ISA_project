package com.example.demo.controller


import com.example.demo.dto.FlightDTO
import com.example.demo.controller.helper.Response
import com.example.demo.models.Flight
import com.example.demo.models.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.service.AvioCompanyService
import com.example.demo.service.FlightService
import com.example.demo.service.LocationService
import com.example.demo.service.UserService

import javax.servlet.http.HttpServletRequest
import java.util.ArrayList

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["flights"])
class FlightController {

    @Autowired
    private lateinit var flightService: FlightService

    @Autowired
    internal lateinit var tokenUtils: TokenUtils

    @Autowired
    private lateinit var avioCompanyService: AvioCompanyService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var locationService: LocationService

    val allFlights: ResponseEntity<List<FlightDTO>>
        @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
        get() {
            val flightsDTO = ArrayList<FlightDTO>()

            flightService.findAll().forEach {
                flightsDTO.add(it.toDTO())
            }

            return ResponseEntity(flightsDTO, HttpStatus.OK)
        }

    @RequestMapping(value = ["/allCompany"], method = [RequestMethod.GET])
    fun getAllCompanyFlights(request: HttpServletRequest): ResponseEntity<List<FlightDTO>> {


        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val avioAdmin = userService.getUser(username)
            ?: return Response.unauthorized()

        val avioCompany = avioCompanyService.findByName(avioAdmin.avioCompany?.name!!)
            ?: return Response.notFound()

        val flightsDTO = ArrayList<FlightDTO>()


        flightService.findByCompany(avioCompany).forEach {
            flightsDTO.add(it.toDTO())
        }

        return ResponseEntity(flightsDTO, HttpStatus.OK)
    }


    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addFlight(request: HttpServletRequest, @RequestBody flightDTO: FlightDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()


        val avioCompany = avioCompanyService.findByName(admin.avioCompany?.name!!)
            ?: return Response.notFound()


        val flight = Flight(
            arrivalTime = flightDTO.arrivalTime,
            departureTime = flightDTO.departureTime,
            company = avioCompany,
            flightTime = flightDTO.flightTime,
            ticketPrice = flightDTO.ticketPrice,
            destination = locationService.findById(flightDTO.locationDTO?.id)
        )


        // TODO check if this is right
        val connectingCities = flight.connectingCities.toMutableList()

        flightDTO.connectedCities.forEach {
            connectingCities.add(locationService.findById(it.id)!!)
        }

        flight.connectingCities = connectingCities.toSet()
        flightService.save(flight)

        return Response.created()
    }
}
