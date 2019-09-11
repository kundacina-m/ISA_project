package com.example.demo.repository

import com.example.demo.models.Friendship
import com.example.demo.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FriendshipRepository : JpaRepository<Friendship, Long> {

    fun findAllByFirstFriendOrSecondFriend(sender: User, receiver: User): List<Friendship>
    fun findAllBySecondFriendAndValid(receiver: User, valid: Boolean): List<Friendship>
    fun findByFirstFriendAndSecondFriend(sender: User, receiver: User): Friendship?
    fun deleteByFirstFriendAndSecondFriend(sender: User, receiver: User): Int?
}
