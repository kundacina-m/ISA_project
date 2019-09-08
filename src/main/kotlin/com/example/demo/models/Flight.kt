package com.example.demo.models

import com.example.demo.dto.FlightDTO
import com.example.demo.dto.LocationDTO
import javax.persistence.*
import java.util.Date
import java.util.HashSet

@Entity
data class Flight(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null,

    @Version
    var version: Long? = null,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "company")
    var company: AvioCompany? = null,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "destination")
    var destination: Location? = null,

    @OneToMany(mappedBy = "connectingFlight", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = false)
    var connectingCities: Set<Location> = HashSet(),

    @Column(name = "departure_time")
    var departureTime: Date? = null,

    @Column(name = "arrival_time")
    var arrivalTime: Date? = null,

    @Column(name = "flight_time")
    var flightTime: Int = 0,

    @Column(name = "ticket_price")
    var ticketPrice: Double = 0.toDouble(),

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var tickets: Set<Ticket> = HashSet()

)

fun Flight.toDTO() =
    FlightDTO(
        id = id,
        version = version,
        arrivalTime = arrivalTime,
        departureTime = departureTime,
        flightTime = flightTime,
        ticketPrice = ticketPrice,
        companyDTO = company?.toDTO(),
        locationDTO = destination?.toDTO(),
        connectedCities = mutableSetOf<LocationDTO>().apply { connectingCities.forEach { add(it.toDTO()) } }
    )
