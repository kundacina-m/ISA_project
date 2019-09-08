package com.example.demo.controller


import com.example.demo.dto.AvioPricesDTO
import com.example.demo.controller.helper.Response
import com.example.demo.models.AvioPrices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.service.AvioCompanyService
import com.example.demo.service.AvioPricesService
import com.example.demo.service.UserService

import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["api/avioprices"])
class AvioPricesController {

    @Autowired
    private lateinit var avioPricesService: AvioPricesService

    @Autowired
    private lateinit var avioCompanyService: AvioCompanyService

    @Autowired
    internal lateinit var tokenUtils: TokenUtils

    @Autowired
    private lateinit var userService: UserService

    @Transactional
    @RequestMapping(value = ["remove"], method = [RequestMethod.PUT])
    fun removePricing(request: HttpServletRequest, @RequestBody avioPricesDTO: AvioPricesDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val avioAdmin = userService.getUser(username)
            ?: return Response.unauthorized()

        avioCompanyService.findByName(avioAdmin.avioCompany?.name!!)
            ?: return Response.notFound()

        val avioPrices = avioPricesService.findById(avioPricesDTO.id)
            ?: return Response.notFound()

        avioPricesService.removeById(avioPrices.id)

        return Response.ok()
    }

    @RequestMapping(value = ["change"], method = [RequestMethod.PUT])
    fun changePricing(request: HttpServletRequest, @RequestBody avioPricesDTO: AvioPricesDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val avioAdmin = userService.getUser(username)
            ?: return Response.unauthorized()

        avioCompanyService.findByName(avioAdmin.avioCompany?.name!!)
            ?: return Response.notFound()

        val pricing = avioPricesService.findById(avioPricesDTO.id)
            ?: return Response.notFound()

        pricing.name = avioPricesDTO.name
        pricing.price = avioPricesDTO.price

        avioPricesService.save(pricing)

        return Response.ok()
    }

    @RequestMapping(value = ["add"], method = [RequestMethod.POST])
    fun addPricing(request: HttpServletRequest, @RequestBody avioPricesDTO: AvioPricesDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = userService.getUser(username)
            ?: return Response.unauthorized()

        val avioCompany = avioCompanyService.findByName(admin.avioCompany?.name!!)
            ?: return Response.notFound()


        val pricing = AvioPrices()

        pricing.avioCompany = avioCompany
        pricing.name = avioPricesDTO.name
        pricing.price = avioPricesDTO.price

        avioPricesService.save(pricing)

        return Response.ok()
    }
}
