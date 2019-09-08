package com.example.demo.models

import javax.persistence.*

@Entity
data class ReservationToken(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) var id: Int? = null,
    var token: String? = null,
    @Column(nullable = false, name = "user_username") var username: String? = null,
    @Column(nullable = false, name = "reservation_id") var reservation: Int? = null
)