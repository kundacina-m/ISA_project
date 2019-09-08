package com.example.demo.models

import javax.persistence.*
import java.sql.Timestamp
import java.util.Calendar
import java.util.Date

@Entity
data class VerificationToken(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) var id: Int? = null,
    var token: String? = null,
    @Column(nullable = false, name = "user_id") var user: Int? = null,
    var expiryDate: Date? = null
) {

    fun calculateExpiryDate(expiryTimeInMinutes: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = Timestamp(cal.time.time)
        cal.add(Calendar.MINUTE, expiryTimeInMinutes)
        return Date(cal.time.time)
    }


    companion object {
        val expiration = 60 * 24
    }
}
