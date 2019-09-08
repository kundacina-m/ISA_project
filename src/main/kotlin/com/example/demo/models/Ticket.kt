package com.example.demo.models

import com.example.demo.dto.TicketDTO
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["seat_number", "flight"])])
data class Ticket(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null,

    @ManyToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER)
    @JoinColumn(name = "flight")
    var flight: Flight? = null,

    @Version
    var version: Long? = null,

    @Column(name = "seat_number", nullable = false)
    var seatNumber: Int = 0,

    @Column(name = "passenger_username", length = 25)
    var passengerUsername: String? = null,

    @Column(name = "passenger_name", length = 25)
    var passengerName: String? = null,

    @Column(name = "passenger_surname", length = 25)
    var passengerLastName: String? = null,

    @Column(name = "passport_number", length = 50)
    var passportNumber: String? = null,

    @Column(name = "fast_reservation_discount", columnDefinition = "Decimal(3, 0) default '0'")
    var fastReservationDiscount: Int = 0,

    @Column(name = "reserved", columnDefinition = "boolean default false")
    var isReserved: Boolean = false
)

fun Ticket.toDTO() =
    TicketDTO(
        id = id,
        version = version,
        fastReservationDiscount = fastReservationDiscount,
        flight = flight?.toDTO(),
        isReserved = isReserved,
        passengerLastName = passengerLastName,
        passengerName = passengerName,
        passengerUsername = passengerUsername,
        passportNumber = passportNumber,
        seatNumber = seatNumber
    )

