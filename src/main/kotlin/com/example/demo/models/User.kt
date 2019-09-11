package com.example.demo.models

import com.example.demo.dto.AuthorityDTO
import com.example.demo.dto.UserDTO
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.userdetails.UserDetails

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.sql.Timestamp
import java.util.HashSet

@Entity
data class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null,

    @NotBlank(message = "Please enter your username")
    @Size(min = 4, message = "Username must be at least 4 characters long")
    @Column(name = "username", unique = true, nullable = false, length = 25)
    private var username: String? = null,

    @NotBlank(message = "Please enter your email")
    @Email(message = "Please enter a valid email")
    @Column(name = "email", unique = true, nullable = false, length = 50)
    var email: String? = null,

    @JsonIgnore
    @Column(name = "pass", nullable = false, length = 250)
    @NotBlank(message = "Please enter your password")
    @Size(min = 5, message = "Your password must be at least 5 characters long")
    private var password: String? = null,

    @NotBlank(message = "Please enter your first name")
    @Size(min = 1, message = "Name is too short")
    @Column(name = "firstName", nullable = false, length = 25)
    var name: String? = null,

    @NotBlank(message = "Please enter your last name")
    @Size(min = 1, message = "Name is too short")
    @Column(name = "lastName", nullable = false, length = 25)
    var surname: String? = null,

    @NotBlank(message = "Please enter city name")
    @Size(min = 1, message = "Name is too short")
    @Column(name = "city", nullable = false, length = 50)
    var city: String? = null,

    @NotBlank(message = "Please enter your phone number")
    @Pattern(regexp = "[0-9]{3}-[0-9]{7}", message = "Number must look like: 064-1234567")
    @Column(name = "phoneNumber", unique = true, nullable = false, length = 15)
    var number: String? = null,

    @Column(name = "enabled", nullable = false)
    private var enabled: Boolean = false,

    @Column(name = "last_password_reset_date")
    var lastPasswordResetDate: Timestamp? = null,

    @Column(name = "pass_change_req", nullable = false)
    var isPassChanged: Boolean = false



) : UserDetails {

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "authority_id", referencedColumnName = "id")])
    private var authorities: List<Authority>? = listOf()

    @OneToMany(mappedBy = "firstFriend", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var friendships: Set<Friendship>? = null

    @OneToMany(mappedBy = "secondFriend", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var friendshipsCopy: Set<Friendship>? = null

    @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinColumn(name = "avio_company")
    var avioCompany: AvioCompany? = null



    // Getters override from UserDetails
    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    override fun getUsername(): String? = username
    override fun isEnabled(): Boolean = this.enabled
    override fun getAuthorities(): List<Authority>? = authorities
    override fun getPassword(): String? = password


    // Setters override from UserDetails
    fun setEnabled(enabled: Boolean) = let { this.enabled = enabled }

    fun setUsername(username: String) = let { this.username = username }
    fun setPassword(password: String) = let { this.password = password }
    fun setAuthorities(authorities: List<Authority>) = let { this.authorities = authorities }
}

fun User.toDTO() =
    UserDTO(
        id = id,
        name = name,
        city = city,
        authority = mutableListOf<AuthorityDTO>().apply { authorities?.forEach { add(it.toDTO()) } },
        avioCompany = avioCompany?.toDTO(),
        email = email,
        isPassChanged = isPassChanged,
        number = number,
        password = password,
        surname = surname,
        username = username
    )

