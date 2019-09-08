package com.example.demo.dto

data class TicketDTO(

    var id: Int? = null,
    var seatNumber: Int = 0,
    var passengerUsername: String? = null,
    var passengerName: String? = null,
    var passengerLastName: String? = null,
    var passportNumber: String? = null,
    var fastReservationDiscount: Int = 0,
    var isReserved: Boolean = false,
    var flight: FlightDTO? = null,
    var version: Long? = null
)
