package com.example.demo.repository

import com.example.demo.models.Authority
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority, Int> {

    fun findByName(name: String): Authority
}
