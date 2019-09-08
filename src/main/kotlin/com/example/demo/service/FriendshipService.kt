package com.example.demo.service

import com.example.demo.models.Friendship
import com.example.demo.models.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import com.example.demo.repository.FriendshipRepository

@Service
open class FriendshipService {

    @Autowired
    internal lateinit var repository: FriendshipRepository

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun findAll(): List<Friendship> = repository.findAll()
    fun findMyFriendships(user: User): List<Friendship> = repository.findAllByFirstFriendOrSecondFriend(user, user)

    fun findAllFriendshipRequests(receiver: User): List<Friendship> =
        repository.findAllBySecondFriendAndValid(receiver, false)

    fun findFriendship(sender: User, receiver: User): Friendship? =
        repository.findByFirstFriendAndSecondFriend(sender, receiver)

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun save(friendship: Friendship): Friendship? = repository.save(friendship)

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    fun deleteFriendship(sender: User, receiver: User): Int? =
        repository.deleteByFirstFriendAndSecondFriend(sender, receiver)

}
