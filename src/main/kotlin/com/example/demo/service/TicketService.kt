package com.example.demo.service

import com.example.demo.models.Ticket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import com.example.demo.repository.TicketRepository

@Service
open class TicketService {

    @Autowired
    private lateinit var repository: TicketRepository

    fun findAll(): List<Ticket> = repository.findAll()
    fun findById(id: Int?): Ticket? = repository.findById(id)

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun save(ticket: Ticket): Ticket = repository.save(ticket)

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun removeById(id: Int?) = let { repository.removeById(id) }

}
