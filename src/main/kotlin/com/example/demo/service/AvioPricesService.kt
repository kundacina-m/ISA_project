package com.example.demo.service

import com.example.demo.models.AvioPrices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import com.example.demo.repository.AvioPricesRepository


@Service
open class AvioPricesService {

    @Autowired
    private lateinit var repository: AvioPricesRepository

    fun findById(id: Int?): AvioPrices? = repository.findById(id)

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun removeById(id: Int?) = let { repository.removeById(id) }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun save(pricing: AvioPrices) = let { repository.save(pricing) }
}
