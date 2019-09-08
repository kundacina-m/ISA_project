package com.example.demo.models

import com.example.demo.dto.AuthorityDTO
import org.springframework.security.core.GrantedAuthority

import javax.persistence.*

@Entity
@Table(name = "authority")
data class Authority(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(unique = true, nullable = false, length = 25)
    var name: String? = null
) : GrantedAuthority {

    override fun getAuthority(): String? = null
}

fun Authority.toDTO() =
    AuthorityDTO(
        id = id!!,
        name = name
    )
