package com.example.demo.service

import com.example.demo.models.Reservation
import com.example.demo.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Service
open class EmailService {

    @Autowired
    private lateinit var mailSender: JavaMailSender

    @Autowired
    private lateinit var enviroment: Environment

    @Async
    @Throws(MailException::class)
    fun sendMail(user: User, tokenValue: String) {
        val uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val controller = "/api/users/completeRegister?token=$tokenValue"

        val url = uri + controller

        val mail = SimpleMailMessage()
        mail.setTo(user.email)
        enviroment!!.getProperty("spring.mail.username")?.let { mail.setFrom(it) }
        mail.setSubject("noreply")
        mail.setText("You have recently made an account on our website." +
            "\r\n\r\nYour credentials are: " +
            "\r\n   username: " + user.username +
            "\r\n   name: " + user.name +
            "\r\n   last name: " + user.surname +
            "\r\n\r\nPlease verify you account by clicking on the link: \r\n" + url
        )
        mailSender!!.send(mail)
    }

    @Async
    @Throws(MailException::class)
    fun sendMailFlightReservation(user: User, reservation: Reservation) {
        var text = "You have recently made a reservation on our website." + "\r\n\r\nYour reservation is: "

        for (ticket in reservation.tickets) {
            text += ("\r\n   Ticket: " +
                "\r\n       Flight: " + (ticket.flight?.company ?: "Error occurred") +
                "\r\n       Price: " + (ticket.flight?.ticketPrice ?: "Error occurred") +
                "\r\n       Destination: " + (ticket.flight?.destination?.airport ?: "Error occurred") + ", "
                + ticket.flight?.destination?.city + ", " + (ticket.flight?.destination?.country ?: "Error occurred") +
                "\r\n       Seat No: " + ticket.seatNumber +
                "\r\n       Passenger: " + ticket.passengerName + " " + ticket.passengerLastName +
                "\r\n       Passport: " + ticket.passportNumber)
        }

        val mail = SimpleMailMessage()
        mail.setTo(user.email)
        enviroment!!.getProperty("spring.mail.username")?.let { mail.setFrom(it) }
        mail.setSubject("noreply")
        mail.setText(text)
        mailSender!!.send(mail)
    }

    @Async
    @Throws(MailException::class)
    fun sendMailFlightReservationAccept(user: User, reservation: Reservation, tokenValue: String) {
        val uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val controller = "/api/reservations/rejectReservation?token=$tokenValue"

        val url = uri + controller


        var text = "You have recently been added to a reservation on our website." + "\r\n\r\nYour reservation is: "

        for (ticket in reservation.tickets) {
            text += ("\r\n   Ticket: " +
                "\r\n       Flight: " + (ticket.flight?.company ?: "Error occurred") +
                "\r\n       Price: " + (ticket.flight?.ticketPrice ?: "Error occurred") +
                "\r\n       Destination: " + (ticket.flight?.destination?.airport ?: "Error occurred") + ", "
                + ticket.flight?.destination?.city + ", " + (ticket.flight?.destination?.country ?: "Error occurred") +
                "\r\n       Seat No: " + ticket.seatNumber +
                "\r\n       Passenger: " + ticket.passengerName + " " + ticket.passengerLastName +
                "\r\n       Passport: " + ticket.passportNumber)
        }

        text += "\r\nTo reject reservation click on the following link: " +
            "\r\n" + url

        val mail = SimpleMailMessage()
        mail.setTo(user.email)
        enviroment!!.getProperty("spring.mail.username")?.let { mail.setFrom(it) }
        mail.setSubject("noreply")
        mail.setText(text)
        mailSender!!.send(mail)
    }
}
