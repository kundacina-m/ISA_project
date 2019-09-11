package com.example.demo.controller

import com.example.demo.dto.EmptyResponse
import com.example.demo.dto.UserDTO
import com.example.demo.controller.helper.Response
import com.example.demo.models.Authority
import com.example.demo.models.User
import com.example.demo.models.toDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import com.example.demo.security.TokenUtils
import com.example.demo.service.AuthorityService
import com.example.demo.service.AvioCompanyService
import com.example.demo.service.UserService
import com.example.demo.service.VerificationTokenService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional
import javax.validation.Valid
import java.io.IOException
import java.security.Principal
import java.util.ArrayList

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping(value = ["api/users"])
class UserController {

    @Autowired
    private lateinit var service: UserService

    @Autowired
    private lateinit var verificationTokenService: VerificationTokenService

    @Autowired
    internal lateinit var tokenUtils: TokenUtils
    @Autowired
    private lateinit var authService: AuthorityService

    @Autowired
    private lateinit var avioService: AvioCompanyService


    val allUsers: ResponseEntity<List<UserDTO>>
        @RequestMapping(value = ["/all"], method = [RequestMethod.GET])
        get() {

            val responseUsers = ArrayList<UserDTO>()

            service.findAll().forEach { responseUsers.add(it.toDTO()) }

            return ResponseEntity(responseUsers, HttpStatus.OK)
        }

    @RequestMapping(value = ["/completeRegister"], method = [RequestMethod.GET])
    @Throws(IOException::class)
    fun completeRegistration(token: String, httpServletResponse: HttpServletResponse): ResponseEntity<Void> {

        val verificationToken = verificationTokenService.findByToken(token)

        val user = service.getUserById(verificationToken.user)
        user.setAuthorities(listOf(Authority(name = "USER")))
        user.isEnabled = true
        service.saveWithoutEncode(user)

        httpServletResponse.sendRedirect("http://localhost:4200")

        return Response.ok()
    }


    @RequestMapping(method = [RequestMethod.POST], consumes = ["application/json"], value = ["/registerUser"])
    fun registerUser(@RequestBody @Valid userDTO: UserDTO, bindingResult: BindingResult): ResponseEntity<EmptyResponse> {

        val temp = User(
            username = userDTO.username,
            password = userDTO.password,
            name = userDTO.name,
            surname = userDTO.surname,
            city = userDTO.city,
            number = userDTO.number,
            email = userDTO.email
        )

        service.save(temp)

//        service.completeRegistration(user.username!!)
        return Response.created()
    }

    @Transactional
    @RequestMapping(method = [RequestMethod.DELETE], value = ["/remove/{username}"])
    fun removeUser(@PathVariable username: String): ResponseEntity<EmptyResponse> =

        try {
            service.remove(username)
            Response.ok()
        } catch (e: Exception) {
            Response.notFound()
        }


    @RequestMapping("/whoami")
    @PreAuthorize("hasRole('USER')")
    fun user(user: Principal): User {
        println("usao sam ovde")
        return service.getUser(user.name)!!
    }

    @RequestMapping(value = ["/changeAuthority"], method = [RequestMethod.PUT])
    fun changeAuthority(request: HttpServletRequest, @RequestBody userDTO: UserDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = service.getUser(username)
            ?: return Response.unauthorized()

        var validAuthority = false

        admin.authorities?.forEach {
            if (it.name.equals("ADMIN")) {
                validAuthority = true
                return@forEach
            }
        }

        if (!validAuthority) return Response.unauthorized()

        val user = service.getUser(userDTO.username!!)
            ?: return Response.unauthorized()

        val auth = user.authorities?.toMutableList()

        userDTO.authority?.forEach {
            auth?.add(authService.findByName(it.name!!))
        }

        user.setAuthorities(auth!!)

        service.saveWithoutEncode(user)

        return Response.ok()
    }

    @RequestMapping(value = ["/removeAuthority"], method = [RequestMethod.POST])
    fun removeAuthority(request: HttpServletRequest, @RequestBody userDTO: UserDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = service.getUser(username) ?: return Response.unauthorized()

        var validAuthority = false

        admin.authorities?.forEach {
            if (it.name.equals("ADMIN")) {
                validAuthority = true
                return@forEach
            }
        }

        if (!validAuthority) return Response.unauthorized()


        val user = service.getUser(userDTO.username!!) ?: return Response.notFound()

        val auth = user.authorities?.toMutableList()

        userDTO.authority?.forEach {
            val index = auth?.indexOf(authService.findByName(it.name!!))
            auth?.removeAt(index!!)
        }

        user.setAuthorities(auth?.toList()!!)

        service.saveWithoutEncode(user)

        return Response.ok()
    }

    @RequestMapping(value = ["/setRole"], method = [RequestMethod.PUT])
    fun setRole(request: HttpServletRequest, @RequestBody userDTO: UserDTO): ResponseEntity<Void> {

        val username = with(tokenUtils) {
            getUsernameFromToken(getToken(request) ?: return Response.unauthorized())
        } ?: return Response.unauthorized()

        val admin = service.getUser(username) ?: return Response.unauthorized()

        var validAuthority = false

        for (a in admin.authorities!!)
            if (a.name.equals("ADMIN")) {
                validAuthority = true
                break
            }

        if (!validAuthority) {
            return Response.notFound()
        }

        val user = service.getUser(userDTO.username!!) ?: return Response.notFound()

        val av = avioService.findByName(userDTO.name!!)

        if (av != null) {
            user.avioCompany = av
        }

        service.saveWithoutEncode(user)

        return Response.ok()
    }
}
