package com.example.demo.controller


import com.example.demo.dto.TicketDTO
import com.example.demo.controller.helper.Response
import com.example.demo.models.Ticket
import com.example.demo.models.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.service.AvioCompanyService
import com.example.demo.service.FlightService
import com.example.demo.service.TicketService
import com.example.demo.service.UserService
import java.util.*

import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["tickets"])
class TicketController {

    @Autowired
    private lateinit var service: TicketService

    @Autowired
    internal lateinit var tokenUtils: TokenUtils

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var avioService: AvioCompanyService

    @Autowired
    private lateinit var flightService: FlightService

    val allTickets: ResponseEntity<List<TicketDTO>>
        @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
        get() {

            val responseTickets = ArrayList<TicketDTO>()

            service.findAll().forEach { ticket ->
                responseTickets.add(ticket.toDTO())
            }

            return ResponseEntity(responseTickets, HttpStatus.OK)
        }

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addTicket(request: HttpServletRequest, @RequestBody ticketDTO: TicketDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username) ?: return Response.unauthorized()

        avioService.findByName(admin.avioCompany?.name!!)
            ?: return Response.unauthorized()


        val flightFromTicket = flightService.findById(ticketDTO.flight?.id)


        val ticket = Ticket().apply {
            fastReservationDiscount = ticketDTO.fastReservationDiscount
            seatNumber = ticketDTO.seatNumber
            isReserved = false
            flight = flightFromTicket
        }

        service.save(ticket)

        return Response.ok()
    }

    @Transactional
    @RequestMapping(value = ["/remove"], method = [RequestMethod.PUT])
    fun removeTicket(request: HttpServletRequest, @RequestBody id: Int): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        avioService.findByName(admin.avioCompany?.name!!)
            ?: return Response.unauthorized()


        service.removeById(id)

        return Response.ok()
    }

    @RequestMapping(value = ["/change"], method = [RequestMethod.PUT])
    fun changeTicket(request: HttpServletRequest, @RequestBody ticketDTO: TicketDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username) ?: return Response.unauthorized()

        avioService.findByName(admin.avioCompany?.name!!)
            ?: return Response.unauthorized()

        val ticket = service.findById(ticketDTO.id) ?: return Response.notFound()

        ticket.seatNumber = ticketDTO.seatNumber
        ticket.fastReservationDiscount = ticketDTO.fastReservationDiscount

        service.save(ticket)

        return Response.ok()
    }
}
