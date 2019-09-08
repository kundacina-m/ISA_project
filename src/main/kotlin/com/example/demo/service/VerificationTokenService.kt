package com.example.demo.service

import com.example.demo.models.VerificationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.demo.repository.VerificationTokenRepository

@Service
open class VerificationTokenService {

    @Autowired
    internal lateinit var repository: VerificationTokenRepository

    fun save(token: VerificationToken): VerificationToken = repository!!.save(token)
    fun findAll(): List<VerificationToken> = repository!!.findAll()
    fun findByToken(token: String): VerificationToken = repository!!.findByToken(token)
    fun deleteByToken(token: String) = repository!!.deleteByToken(token)

}