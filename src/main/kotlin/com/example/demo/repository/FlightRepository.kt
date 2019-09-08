package com.example.demo.repository

import com.example.demo.models.AvioCompany
import com.example.demo.models.Flight
import org.springframework.data.jpa.repository.JpaRepository

interface FlightRepository : JpaRepository<Flight, Long> {

    fun findById(id: Int?): Flight
    fun findByCompany(company: AvioCompany): List<Flight>
}
