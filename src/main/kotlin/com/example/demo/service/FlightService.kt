package com.example.demo.service

import com.example.demo.models.AvioCompany
import com.example.demo.models.Flight
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import com.example.demo.repository.FlightRepository

@Service
open class FlightService {

    @Autowired
    private lateinit var repository: FlightRepository

    fun findAll(): List<Flight> = repository.findAll()
    fun findById(id: Int?): Flight = repository.findById(id)
    fun findByCompany(company: AvioCompany): List<Flight> = repository.findByCompany(company)

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun save(flight: Flight) = let { repository.save(flight) }

}
