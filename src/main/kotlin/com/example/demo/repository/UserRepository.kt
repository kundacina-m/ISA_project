package com.example.demo.repository

import com.example.demo.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsernameIgnoreCase(username: String): User
    fun findByUsernameAndEnabled(username: String, enabled: Boolean): User
    fun findById(id: Int?): User
    fun deleteByUsernameIgnoreCase(username: String)
    fun findByEmail(email: String): User
}
