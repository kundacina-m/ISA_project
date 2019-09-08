package com.example.demo.repository

import com.example.demo.models.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketRepository : JpaRepository<Ticket, Long> {

    fun findById(id: Int?): Ticket
    fun removeById(id: Int?)
}
