package com.example.demo.controller


import com.example.demo.dto.AvioCompanyDTO
import com.example.demo.dto.LocationDTO
import com.example.demo.dto.TicketDTO
import com.example.demo.controller.helper.Response
import com.example.demo.models.AvioCompany
import com.example.demo.models.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.service.AvioCompanyService
import com.example.demo.service.LocationService
import com.example.demo.service.TicketService
import com.example.demo.service.UserService

import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional
import java.util.ArrayList

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["api/aviocompanies"])
class AvioCompanyController {

    @Autowired
    private lateinit var avioCompanyService: AvioCompanyService

    @Autowired
    internal lateinit var tokenUtils: TokenUtils

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var locationService: LocationService

    @Autowired
    private lateinit var ticketService: TicketService

    val allAvioCompanies: ResponseEntity<List<AvioCompanyDTO>>
        @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
        get() {
            val responseCompanies = ArrayList<AvioCompanyDTO>()

            avioCompanyService.findAll().forEach {
                responseCompanies.add(it.toDTO())
            }

            return ResponseEntity(responseCompanies, HttpStatus.OK)
        }

    @RequestMapping(value = ["/allNonResTickets"], method = [RequestMethod.GET])
    fun getAllNonReservedTickets(request: HttpServletRequest): ResponseEntity<List<TicketDTO>> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        val avioCompany = avioCompanyService.findByName(admin.avioCompany?.name!!)
            ?: return Response.notFound()

        val responseTickets = ArrayList<TicketDTO>()

        ticketService.findAll().forEach { ticket ->
            avioCompany.flights.forEach { flight ->
                if (ticket.flight?.id?.equals(flight.id)!!) {
                    if (!ticket.isReserved)
                        responseTickets.add(ticket.toDTO())
                }
            }
        }
        return ResponseEntity(responseTickets, HttpStatus.OK)
    }

    @RequestMapping(value = ["/allOurTickets"], method = [RequestMethod.GET])
    fun getAllTickets(request: HttpServletRequest): ResponseEntity<List<TicketDTO>> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        val avioCompany = avioCompanyService.findByName(admin.avioCompany?.name!!)
            ?: return Response.notFound()

        val responseTickets = ArrayList<TicketDTO>()


        ticketService.findAll().forEach { ticket ->

            println(ticket.flight)
            print(avioCompany.flights.size)
            avioCompany.flights.forEach { flight ->
                print(flight)
                if (ticket.flight?.id?.equals(flight.id)!!)
                    responseTickets.add(ticket.toDTO())
            }
        }

        return ResponseEntity(responseTickets, HttpStatus.OK)
    }


    @RequestMapping(value = ["/allNotMine"], method = [RequestMethod.GET])
    fun getAllAvioCompaniesNotMine(request: HttpServletRequest): ResponseEntity<List<LocationDTO>> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        val avioCompany = avioCompanyService.findByName(admin.avioCompany?.name!!)
            ?: return Response.notFound()

        val locationsResponse = ArrayList<LocationDTO>()

        var validAuthority = false

        admin.authorities!!.forEach {
            if (it.name.equals("AVIO_ADMIN")) {
                validAuthority = true
                return@forEach
            }
        }

        if (!validAuthority) return Response.notFound()

        locationService.findAll().forEach { location ->
            if (!avioCompany.locations.contains(location))
                locationsResponse.add(location.toDTO())
        }

        return ResponseEntity(locationsResponse, HttpStatus.OK)
    }

    @Transactional
    @RequestMapping(value = ["/remove"], method = [RequestMethod.PUT])
    fun removeLocation(request: HttpServletRequest, @RequestBody locationDTO: LocationDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        val location = locationService.findById(locationDTO.id)
            ?: return Response.notFound()

        val avioCompany = avioCompanyService.findByName(admin.avioCompany?.name!!)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        var validAuthority = false

        admin.authorities!!.forEach {
            if (it.name.equals("AVIO_ADMIN")) {
                println(it.name)
                validAuthority = true
                return@forEach
            }
        }


        if (!validAuthority) return Response.notFound()

        val pastLocations = avioCompany.locations.toMutableList().apply { remove(location) }
        avioCompany.locations = pastLocations.toMutableSet()

        avioCompanyService.save(avioCompany)

        return Response.ok()
    }

    @RequestMapping(value = ["/addToAvio"], method = [RequestMethod.PUT])
    fun addLocation(request: HttpServletRequest, @RequestBody locationDTO: LocationDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        val location = locationService.findById(locationDTO.id)
            ?: return Response.notFound()

        val avioCompany = avioCompanyService.findByName(admin.avioCompany?.name!!)
            ?: return Response.notFound()


        var validAuthority = false

        admin.authorities?.forEach {
            if (it.name.equals("AVIO_ADMIN")) {
                println(it.name)
                validAuthority = true
                return@forEach
            }
        }

        if (!validAuthority) return Response.notFound()

        val pastLocations = avioCompany.locations.toMutableList().apply { add(location) }
        avioCompany.locations = pastLocations.toSet()

        avioCompanyService.save(avioCompany)

        return Response.ok()
    }

    @RequestMapping(value = ["/changeInfo"], method = [RequestMethod.PUT])
    fun changeCompanyInfo(request: HttpServletRequest, @RequestBody avioCompanyDTO: AvioCompanyDTO): ResponseEntity<AvioCompanyDTO> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        var validAuthority = false

        admin.authorities?.forEach {
            if (it.name.equals("AVIO_ADMIN")) {
                println(it.name)
                validAuthority = true
                return@forEach
            }
        }

        if (!validAuthority) {
            println("IZBACIO 1")
            return Response.notFound()
        } else if (!admin.avioCompany?.id?.equals(avioCompanyDTO.id)!!) {
            println("IZBACIO 2")
            println(admin.avioCompany?.id.toString())
            println(avioCompanyDTO.id)
            return Response.notFound()        }

        val avioCompany = avioCompanyService.findByName(avioCompanyDTO.name!!)
            ?: return Response.notFound()

        avioCompany.name = avioCompanyDTO.name
        avioCompany.address = avioCompanyDTO.address
        avioCompany.promoDescription = avioCompanyDTO.promoDescription

        avioCompanyService.save(avioCompany)

        return ResponseEntity(avioCompany.toDTO(), HttpStatus.OK)
    }

    @RequestMapping(value = ["/addCompany"], method = [RequestMethod.POST])
    fun addAvioCompany(request: HttpServletRequest, @RequestBody avioCompanyDTO: AvioCompanyDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        var validAuthority = false

        admin.authorities?.forEach {
            if (it.name.equals("ADMIN")) {
                validAuthority = true
                return@forEach
            }
        }


        if (!validAuthority) return Response.unauthorized()

        val avio = AvioCompany()

        avio.name = avioCompanyDTO.name
        avio.address = avioCompanyDTO.address
        avio.promoDescription = avioCompanyDTO.promoDescription
        avio.image = "universal.jpg"

        avioCompanyService.save(avio)

        return Response.ok()
    }
}
