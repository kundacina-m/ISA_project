package com.example.demo.service

import com.example.demo.models.ReservationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.demo.repository.ReservationTokenRepository

@Service
open class ReservationTokenService {

    @Autowired
    private lateinit var repository: ReservationTokenRepository

    fun save(reservationToken: ReservationToken): ReservationToken = repository.save(reservationToken)
    fun findByToken(token: String): ReservationToken = repository.findByToken(token)
}
