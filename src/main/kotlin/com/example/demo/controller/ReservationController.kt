package com.example.demo.controller

import com.example.demo.dto.ReservationDTO
import com.example.demo.dto.TicketDTO
import com.example.demo.controller.helper.Response
import com.example.demo.models.Reservation
import com.example.demo.models.ReservationToken
import com.example.demo.models.Ticket
import com.example.demo.models.toDTO

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.service.*

import javax.servlet.http.HttpServletRequest
import java.util.*

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["reservations"])
class ReservationController {

    @Autowired
    internal lateinit var tokenUtils: TokenUtils

    @Autowired
    private lateinit var ticketService: TicketService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var reservationService: ReservationService

    @Autowired
    internal lateinit var emailService: EmailService

    @Autowired
    private lateinit var reservationTokenService: ReservationTokenService

    val allReservations: ResponseEntity<List<ReservationDTO>>
        @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
        get() {
            val reservationsResponse = ArrayList<ReservationDTO>()

            reservationService.findAll().forEach {
                reservationsResponse.add(it.toDTO())
            }

            return ResponseEntity(reservationsResponse, HttpStatus.OK)
        }

    @RequestMapping(value = ["/reserve"], method = [RequestMethod.POST])
    fun reserveFlight(request: HttpServletRequest, @RequestBody ticketsRequest: Set<TicketDTO>): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val me = userService.getUser(username) ?: return Response.unauthorized()
        val tickets = HashSet<Ticket>()

        ticketsRequest.forEach { ticketRequested ->
            val ticket = ticketService.findById(ticketRequested.id)
                ?: return Response.notFound()

            ticket.passengerLastName = ticketRequested.passengerLastName
            ticket.passengerName = ticketRequested.passengerName
            ticket.passengerUsername = ticketRequested.passengerUsername
            ticket.passportNumber = ticketRequested.passportNumber
            ticket.isReserved = true


            tickets.add(ticket)
        }

        val reservation = Reservation()


        val temp = reservation.tickets.toMutableSet()

        tickets.forEach {
            ticketService.save(it)
            temp.add(it)
        }

        reservation.tickets = temp.toSet()

        reservationService.save(reservation)

        try {
            emailService.sendMailFlightReservation(me, reservation)
        } catch (e: Exception) {
            println("Greska prilikom slanja emaila: " + e.message)
        }

        reservation.tickets.forEach { ticket ->
            if (ticket.passengerUsername != null)
                if (!ticket.passengerUsername.equals(username))
                    acceptReservation(reservation.id, ticket.passengerUsername!!)

        }

        return Response.created()
    }

    fun acceptReservation(reservationId: Int?, username: String) {

        val user = userService.getUser(username) ?: return

        val uuid = UUID.randomUUID().toString()

        val token = ReservationToken()

        token.reservation = reservationId
        token.token = uuid
        token.username = username

        reservationTokenService.save(token)

        val token1 = reservationTokenService.findByToken(token.token!!)
        val reservation = reservationService.findById(token1.reservation)


        try {
            emailService.sendMailFlightReservationAccept(user, reservation, uuid)
        } catch (e: Exception) {
            println("Greska prilikom slanja emaila: " + e.message)
        }

    }

    @RequestMapping(value = ["/rejectReservation"], method = [RequestMethod.GET])
    fun rejectReservation(token: String): ResponseEntity<Void> {
        val reservationToken = reservationTokenService.findByToken(token)
        val username = reservationToken.username

        val reservation = reservationService.findById(reservationToken.reservation)

        var friendTicket: Ticket

        reservation.tickets.forEach { ticket ->
            if (ticket.passengerUsername.equals(username)) {
                friendTicket = ticket
                friendTicket.passengerLastName = null
                friendTicket.passengerName = null
                friendTicket.passengerUsername = null
                friendTicket.isReserved = false

                ticketService.save(friendTicket)
            }
        }

        return Response.ok()
    }
}
