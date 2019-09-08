package com.example.demo.repository

import com.example.demo.models.ReservationToken
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationTokenRepository : JpaRepository<ReservationToken, Long> {

    fun findByToken(token: String): ReservationToken
}
