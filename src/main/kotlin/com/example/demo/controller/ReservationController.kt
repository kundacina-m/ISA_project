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
@RequestMapping(value = ["api/reservations"])
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

    val allReservation: ResponseEntity<List<ReservationDTO>>
        @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
        get() {
            val reservationsResponse = ArrayList<ReservationDTO>()

            reservationService.findAll().forEach {
                reservationsResponse.add(it.toDTO())
            }

            return ResponseEntity(reservationsResponse, HttpStatus.OK)
        }

    @RequestMapping(value = ["/reserveFlight"], method = [RequestMethod.POST])
    fun reserveFlight(request: HttpServletRequest, @RequestBody ticketsDTO: Set<TicketDTO>): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val me = userService.getUser(username) ?: return Response.unauthorized()
        val tickets = HashSet<Ticket>()

        for (ticketDTO in ticketsDTO) {
            println("CCCCCCCCCCCCCC " + ticketDTO.passengerUsername)
            val ticket = ticketService.findById(ticketDTO.id)
                ?: return Response.notFound()

            ticket.passengerLastName = ticketDTO.passengerLastName
            ticket.passengerName = ticketDTO.passengerName
            ticket.passengerUsername = ticketDTO.passengerUsername
            ticket.passportNumber = ticketDTO.passportNumber
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

        println("RESERVATION TICKET SIZE " + reservation.tickets.size)
        reservationService.save(reservation)

        try {
            emailService.sendMailFlightReservation(me, reservation)
        } catch (e: Exception) {
            println("Greska prilikom slanja emaila: " + e.message)
        }

        for (ticket in reservation.tickets) {
            if (ticket.passengerUsername != null)
                if (!ticket.passengerUsername.equals(username)) {
                    println("passenger username " + ticket.passengerUsername)
                    println("passenger ticket size " + reservation.tickets.size)
                    acceptReservation(reservation.id, ticket.passengerUsername!!)
                }
        }
        val res = reservationService.findById(reservation.id)
        println("NANIIIIIII " + res.tickets.size)
        return Response.created()
    }

    fun acceptReservation(reservationId: Int?, username: String) {

        val user = userService.getUser(username) ?: return

        println("BBBBBB $username")

        val uuid = UUID.randomUUID().toString()

        val token = ReservationToken()

        token.reservation = reservationId
        token.token = uuid
        token.username = username

        reservationTokenService.save(token)

        val token1 = reservationTokenService.findByToken(token.token!!)
        val reservation = reservationService.findById(token1.reservation)


        println("NANIIIII OPET : " + reservation.tickets.size)

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

        println("aj molim te $token")
        println("gggggg " + reservationToken.reservation)

        val reservation = reservationService.findById(reservationToken.reservation)

        println("AAAAAAAAAAAAAAAAAAA " + reservation.tickets.size)

        var friendTicket: Ticket

        for (ticket in reservation.tickets) {
            println(ticket.passengerUsername)
            println("USERNAME $username")
            if (ticket.passengerUsername.equals(username)) {
                println("USAO OVDE")
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
