package com.example.demo.service


import com.example.demo.models.Authority
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.demo.repository.AuthorityRepository

@Service
open class AuthorityService {

    @Autowired
    private lateinit var authorityRepository: AuthorityRepository

    fun findAll(): List<Authority> = authorityRepository.findAll()
    fun findByName(name: String): Authority = authorityRepository.findByName(name)

}