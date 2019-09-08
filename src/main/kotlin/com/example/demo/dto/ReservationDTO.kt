package com.example.demo.dto

import java.util.HashSet

data class ReservationDTO(

    var id: Int? = null,
    var tickets: MutableSet<TicketDTO> = HashSet()
)
