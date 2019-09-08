package com.example.demo.controller


import com.example.demo.dto.FriendshipDTO
import com.example.demo.controller.helper.Response
import com.example.demo.models.Friendship
import com.example.demo.models.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.service.FriendshipService
import com.example.demo.service.UserService

import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional
import java.util.ArrayList

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["friendships"])
class FriendshipController {

    @Autowired
    private lateinit var friendshipService: FriendshipService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    internal lateinit var tokenUtils: TokenUtils


    @RequestMapping(value = ["/allFriendships"], method = [RequestMethod.GET])
    fun getAllFriendships(request: HttpServletRequest): ResponseEntity<List<FriendshipDTO>> {
        val friendsResponse = ArrayList<FriendshipDTO>()

        friendshipService.findAll().forEach {
            friendsResponse.add(it.toDTO())
        }
        return ResponseEntity(friendsResponse, HttpStatus.OK)
    }

    @RequestMapping(value = ["/allMyFriends"], method = [RequestMethod.GET])
    fun getAllMyFriends(request: HttpServletRequest): ResponseEntity<List<String>> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val user = userService.getUser(username)
            ?: return Response.unauthorized()

        val friendships = friendshipService.findMyFriendships(user)
        val friendsResponse = ArrayList<String>()

        friendships.forEach {
            if (it.valid) {
                if (!it.firstFriend?.username.equals(username))
                    friendsResponse.add(it.firstFriend?.username!!)
                else
                    friendsResponse.add(it.secondFriend?.username!!)
            }
        }
        return ResponseEntity(friendsResponse, HttpStatus.OK)
    }

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    fun addFriend(request: HttpServletRequest, @RequestBody friendUsername: String): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()


        val friendship = Friendship(
            firstFriend = userService.getUser(username),
            secondFriend = userService.getUser(friendUsername)
        )

        return if (friendshipService.save(friendship) != null) Response.created()
        else Response.notFound()
    }

    @RequestMapping(value = ["/accept"], method = [RequestMethod.PUT])
    fun acceptFriend(request: HttpServletRequest, @RequestBody friendUsername: String): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val friendship = friendshipService.findFriendship(
            sender = userService.getUser(friendUsername)!!,
            receiver =  userService.getUser(username)!!
        ) ?: return Response.notFound()

        friendship.valid = true
        friendshipService.save(friendship)


        return Response.ok()
    }

    @Transactional
    @RequestMapping(value = ["/remove"], method = [RequestMethod.PUT])
    fun removeFriend(request: HttpServletRequest, @RequestBody friendUsername: String): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val me = userService.getUser(username)
            ?: return Response.unauthorized()

        val friend = userService.getUser(friendUsername)
            ?: return Response.notFound()

        when {
            friendshipService.findFriendship(friend, me) != null -> friendshipService.deleteFriendship(friend, me)
            friendshipService.findFriendship(me, friend) != null -> friendshipService.deleteFriendship(me, friend)
            else -> return Response.notFound()
        }

        return Response.ok()
    }

    @RequestMapping(value = ["/requests"], method = [RequestMethod.GET])
    fun getFriendRequests(request: HttpServletRequest): ResponseEntity<List<FriendshipDTO>> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val friendships = friendshipService.findAllFriendshipRequests(userService.getUser(username)!!)
        val friendshipsResponse = ArrayList<FriendshipDTO>()

        friendships.forEach {
            friendshipsResponse.add(it.toDTO())
        }

        return ResponseEntity(friendshipsResponse, HttpStatus.OK)
    }

}
