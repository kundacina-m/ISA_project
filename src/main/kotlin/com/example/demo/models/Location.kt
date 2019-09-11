package com.example.demo.models

import com.example.demo.dto.LocationDTO
import javax.persistence.*
import java.util.HashSet

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["id", "city", "country"])])
data class Location(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null,

    @Version
    var version: Long? = null,



    @Column(name = "airport", nullable = false, length = 25)
    var airport: String? = null,

    @Column(name = "city", nullable = false, length = 25)
    var city: String? = null,

    @Column(name = "country", nullable = false, length = 25)
    var country: String? = null

) {
    @ManyToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER)
    @JoinColumn(name = "connecting_flight")
    var connectingFlight: Flight? = null

    @OneToMany(mappedBy = "destination", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var flightsDestination: Set<Flight> = HashSet()
}

fun Location.toDTO() =
    LocationDTO(
        id = id,
        airport = airport,
        city = city,
        country = country,
        version = version
    )


