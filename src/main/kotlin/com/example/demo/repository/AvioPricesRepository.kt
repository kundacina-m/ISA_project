package com.example.demo.repository

import com.example.demo.models.AvioPrices
import org.springframework.data.jpa.repository.JpaRepository

interface AvioPricesRepository : JpaRepository<AvioPrices, Long> {

    fun removeById(id: Int?)
    fun findById(id: Int?): AvioPrices
}
