package com.example.demo.service

import com.example.demo.models.Reservation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.demo.repository.ReservationRepository

@Service
open class ReservationService {

    @Autowired
    private lateinit var repository: ReservationRepository

    fun findAll(): List<Reservation> = repository.findAll()
    fun save(reservation: Reservation): Reservation = repository.save(reservation)
    fun findById(id: Int?): Reservation = repository.findById(id)
}
