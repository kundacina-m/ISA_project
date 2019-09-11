package com.example.demo.service


import com.example.demo.models.AvioCompany
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.example.demo.repository.AvioCompanyRepository

@Service
open class AvioCompanyService {

    @Autowired
    private lateinit var repository: AvioCompanyRepository

    fun findAll(): List<AvioCompany> = repository.findAll()
    fun findByName(name: String): AvioCompany? = repository.findByName(name)
    fun save(avioCompany: AvioCompany): AvioCompany? = repository.save(avioCompany)

}
