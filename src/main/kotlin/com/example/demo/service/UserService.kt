package com.example.demo.service

import com.example.demo.models.User
import com.example.demo.models.VerificationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import com.example.demo.repository.UserRepository

import java.util.Date
import java.util.UUID

@Service
open class UserService : UserDetailsService {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var verificationTokenService: VerificationTokenService

    @Autowired
    internal lateinit var emailService: EmailService

    fun findAll(): List<User> = repository.findAll()
    fun getUser(username: String): User? = repository.findByUsernameAndEnabled(username, true)
    fun getUserById(id: Int?): User = repository.findById(id)
    fun remove(username: String) = let { repository.deleteByUsernameIgnoreCase(username) }
    fun saveWithoutEncode(user: User): User = repository.save(user)

    fun save(user: User): User {
        user.setPassword(passwordEncoder.encode(user.password))
        return repository.save(user)
    }


    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails =
        repository.findByUsernameAndEnabled(username, true)

    fun changePassword(oldPassword: String, newPassword: String, username: String) {
        val user = loadUserByUsername(username) as User

        if (passwordEncoder.matches(oldPassword, user.password)) {
            user.setPassword(passwordEncoder.encode(newPassword))
            repository.save(user)
        }
    }

    fun completeRegistration(username: String) {

        val user = loadUserByUsername(username) as User

        val token = VerificationToken()
        token.user = user.id
        token.expiryDate = token.calculateExpiryDate(Date().time.toInt())
        token.token = UUID.randomUUID().toString()

        verificationTokenService.save(token)


//        try {
//            emailService!!.sendMail(user, token.token!!)
//        } catch (e: Exception) {
//            println("Greska prilikom slanja emaila: " + e.message)
//        }

    }
}
