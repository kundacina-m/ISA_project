package com.example.demo.repository

import com.example.demo.models.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationTokenRepository : JpaRepository<VerificationToken, Long> {
    fun findByToken(token: String): VerificationToken
    fun deleteByToken(token: String)
}
