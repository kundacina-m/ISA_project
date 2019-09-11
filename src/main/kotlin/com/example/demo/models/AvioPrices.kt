package com.example.demo.models

import com.example.demo.dto.AvioPricesDTO
import javax.persistence.*

@Entity
@Table
data class AvioPrices(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null,

    @Version
    var version: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "price")
    var price: Float = 0.toFloat()
) {
    @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinColumn(name = "avio_company")
    var avioCompany: AvioCompany? = null
}

fun AvioPrices.toDTO() =
    AvioPricesDTO(
        id = id,
        version = version,
        name = name,
        price = price
    )
