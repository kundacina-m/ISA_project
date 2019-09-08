package com.example.demo.repository

import com.example.demo.models.AvioCompany
import org.springframework.data.jpa.repository.JpaRepository

interface AvioCompanyRepository : JpaRepository<AvioCompany, Long> {

    fun findByName(name: String): AvioCompany
}
