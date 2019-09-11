package com.example.demo.controller


import com.example.demo.dto.EmptyResponse
import com.example.demo.dto.UserDTO
import com.example.demo.controller.helper.Response
import com.example.demo.models.User
import com.example.demo.models.UserTokenState
import com.example.demo.models.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.security.auth.JwtAuthenticationRequest
import com.example.demo.service.FriendshipService
import com.example.demo.service.UserService

import javax.servlet.http.HttpServletRequest
import java.io.IOException
import java.util.ArrayList

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["api/auth"])
class AuthenticationController {

    @Autowired
    internal lateinit var tokenUtils: TokenUtils

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userDetailsService: UserService

    @Autowired
    private lateinit var friendshipService: FriendshipService

    @RequestMapping(method = [RequestMethod.GET], value = ["/profile"], produces = ["application/json"])
    fun getProfile(request: HttpServletRequest): ResponseEntity<UserDTO> {

        println(request.getHeader("Authorization"))

        with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        }?.let {
            return ResponseEntity(userDetailsService.getUser(it)?.toDTO()!!, HttpStatus.OK)
        } ?: return Response.unauthorized()

    }

    @RequestMapping(value = ["/allNonFriends"], method = [RequestMethod.GET])
    fun getAllUsersNonFriends(request: HttpServletRequest): ResponseEntity<List<UserDTO>> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()


        val friendships = friendshipService.findMyFriendships(userDetailsService.getUser(username)!!)
        val nonFriendsResponse = ArrayList<UserDTO>()

        userDetailsService.findAll().forEach { user ->
            var notFriendsWith = true

            friendships.forEach { friend ->
                if (friend.firstFriend?.username.equals(user.username) ||
                    friend.secondFriend?.username.equals(user.username))
                    notFriendsWith = false
            }


            if (!user.username.equals(username) && notFriendsWith)
                nonFriendsResponse.add(user.toDTO())
        }

        return ResponseEntity(nonFriendsResponse, HttpStatus.OK)
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    @Throws(AuthenticationException::class, IOException::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: JwtAuthenticationRequest): ResponseEntity<*> {

        println(authenticationRequest.username + " " + authenticationRequest.password)

        try {
            val authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(
                    authenticationRequest.username,
                    authenticationRequest.password)
                )

            SecurityContextHolder.getContext().authentication = authentication

            // Kreiraj token
            val user = authentication.principal as User
            val jwt = tokenUtils.generateToken(user.username!!)
            val expiresIn = tokenUtils.expiredIn

            return ResponseEntity.ok(UserTokenState(jwt, expiresIn.toLong()))
        } catch (e: BadCredentialsException) {
            return Response.unauthorized<EmptyResponse>()
        }

    }

    @RequestMapping(value = ["/changeInfo"], method = [RequestMethod.POST], consumes = ["application/json"])
    fun changeUserInfo(request: HttpServletRequest, @RequestBody userDTO: UserDTO): ResponseEntity<UserDTO> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val user = userDetailsService.getUser(username) ?: return Response.notFound()
        user.email = userDTO.email
        user.name = userDTO.name
        user.surname = userDTO.surname
        user.city = userDTO.city
        user.number = userDTO.number

        userDetailsService.saveWithoutEncode(user)

        return ResponseEntity(user.toDTO(), HttpStatus.OK)
    }

    @RequestMapping(value = ["/changePassword"], method = [RequestMethod.POST])
    fun changePassword(@RequestBody passwordChanger: PasswordChanger, request: HttpServletRequest): ResponseEntity<EmptyResponse> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        userDetailsService.changePassword(passwordChanger.oldPassword!!, passwordChanger.newPassword!!, username)

        return Response.ok()
    }

    class PasswordChanger {
        var oldPassword: String? = null
        var newPassword: String? = null
    }

}