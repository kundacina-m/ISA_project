package com.example.demo.dto

import java.util.HashSet

data class AvioCompanyDTO(

    var id: Int? = null,
    var name: String? = null,
    var address: String? = null,
    var promoDescription: String? = null,
    var locations: MutableSet<LocationDTO> = HashSet(),
    var image: String? = null,
    var prices: MutableSet<AvioPricesDTO> = HashSet()
)
