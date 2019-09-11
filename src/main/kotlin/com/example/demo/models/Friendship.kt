package com.example.demo.models

import com.example.demo.dto.FriendshipDTO
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["id", "sender", "receiver"])])
data class Friendship(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null,


    @Column(nullable = false, name = "valid")
    var valid: Boolean = false,

    @Version
    var version: Long? = null

){

    @ManyToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER)
    @JoinColumn(name = "sender", nullable = false)
    var firstFriend: User? = null

    @ManyToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver", nullable = false)
    var secondFriend: User? = null
}

fun Friendship.toDTO() =
    FriendshipDTO(
        version = version,
        sender = firstFriend?.toDTO(),
        receiver = secondFriend?.toDTO(),
        isValid = valid
    )

