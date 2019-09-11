package com.example.demo.models

import com.example.demo.dto.ReservationDTO
import com.example.demo.dto.TicketDTO
import javax.persistence.*
import java.util.HashSet

@Entity
@Table
data class Reservation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false) var id: Int? = null
) {
    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER, cascade = [CascadeType.ALL]) var tickets: Set<Ticket> = HashSet()

}

fun Reservation.toDTO() =
    ReservationDTO(
        id = id,
        tickets = mutableSetOf<TicketDTO>().apply { tickets.forEach { add(it.toDTO()) } }
    )