package com.example.demo.service


import com.example.demo.models.Location
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import com.example.demo.repository.LocationRepository

@Service
open class LocationService {

    @Autowired
    private lateinit var repository: LocationRepository

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun findAll(): List<Location> = repository.findAll()
    fun findById(id: Int?): Location? = repository.findById(id)

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun delete(id: Int?) = let { repository.removeById(id) }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun save(location: Location) = let { repository.save(location) }

}
