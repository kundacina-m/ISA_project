package com.example.demo.repository

import com.example.demo.models.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<Reservation, Long> {

    fun findById(id: Int?): Reservation
}
