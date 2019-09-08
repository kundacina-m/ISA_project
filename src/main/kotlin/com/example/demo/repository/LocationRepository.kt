package com.example.demo.repository

import com.example.demo.models.Location
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository : JpaRepository<Location, Long> {

    fun removeById(id: Int?)
    fun findById(id: Int?): Location
}
