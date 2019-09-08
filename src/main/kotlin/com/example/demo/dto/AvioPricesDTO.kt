package com.example.demo.dto


data class AvioPricesDTO(
    var id: Int? = null,
    var name: String? = null,
    var price: Float = 0.toFloat(),
    var version: Long? = null
)
