package com.example.demo.models

import com.example.demo.dto.AvioCompanyDTO
import com.example.demo.dto.AvioPricesDTO
import com.example.demo.dto.LocationDTO
import javax.persistence.*
import java.util.HashSet

@Entity(name = "avio_company")
data class AvioCompany(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null,

    @Column(name = "name", unique = true, nullable = false, length = 50)
    var name: String? = null,

    @Column(name = "address", columnDefinition = "varchar(50) default 'UNKNOWN'")
    var address: String? = null,

    @Column(name = "promo_description", columnDefinition = "varchar(500) default 'UNKNOWN'")
    var promoDescription: String? = null,

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var flights: Set<Flight> = HashSet(),

    @OneToMany(mappedBy = "avioCompany", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var admins: Set<User> = HashSet(),

    @OneToMany(mappedBy = "avioCompany", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var prices: Set<AvioPrices> = HashSet(),

    @ManyToMany
    @JoinTable(name = "location_avio_company", joinColumns = [JoinColumn(name = "avio_company_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "location_id", referencedColumnName = "id")])
    var locations: Set<Location> = HashSet(),

    @Column(name = "image", length = 50)
    var image: String? = null

)

fun AvioCompany.toDTO() =
    AvioCompanyDTO(
        id = id,
        address = address ?: "No address",
        image = image ?: "No image",
        name = name ?: "No name",
        promoDescription = promoDescription ?: "No description",
        locations = mutableSetOf<LocationDTO>().apply { locations.forEach { add(it.toDTO()) } },
        prices = mutableSetOf<AvioPricesDTO>().apply { prices.forEach { add(it.toDTO()) } }
    )
