package com.example.demo.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import com.example.demo.models.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import java.util.Date

@Component
open class TokenUtils {

    @Value("spring-com.example.demo.security-demo")
    private val app: String? = null

    @Value("entersecretthatwillbeusedforthisprojectbooking")
    var secret: String? = null

    @Value("300")
    val expiredIn: Int = 0

    @Value("Authorization")
    private val authHeader: String? = null

    private val signatureAlgorithm = SignatureAlgorithm.HS512

    fun generateToken(username: String): String =
        Jwts.builder()
            .setIssuer(app)
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(generateExpirationDate())
            .signWith(signatureAlgorithm, secret).compact()


    private fun generateExpirationDate() = Date(Date().time + 2000000)

    fun refreshToken(token: String): String? {
        var refreshedToken: String?
        try {
            val claims = getAllClaimsFromToken(token)
            claims!!.issuedAt = Date()
            refreshedToken = Jwts.builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, secret).compact()
        } catch (e: Exception) {
            refreshedToken = null
        }

        return refreshedToken
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date): Boolean? =
        !isCreatedBeforeLastPasswordReset(getIssuedAtDateFromToken(token), lastPasswordReset) && !isTokenExpired(token)


    fun validateToken(token: String, userDetails: UserDetails): Boolean? {
        val user = userDetails as User
        val username = getUsernameFromToken(token)
        val created = getIssuedAtDateFromToken(token)

        return (username != null && username == userDetails.username
            && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate))
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date?, lastPasswordReset: Date?): Boolean =
        lastPasswordReset != null && created!!.before(lastPasswordReset)


    private fun isTokenExpired(token: String): Boolean =
        getExpirationDateFromToken(token)?.before(Date())!!


    private fun getAllClaimsFromToken(token: String): Claims? {
        var claims: Claims?
        try {
            claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            claims = null
        }

        return claims
    }

    fun getUsernameFromToken(token: String): String? {
        var username: String?
        try {
            val claims = this.getAllClaimsFromToken(token)
            username = claims!!.subject
        } catch (e: Exception) {
            username = null
        }

        return username
    }

    fun getIssuedAtDateFromToken(token: String): Date? {
        var issueAt: Date?
        try {
            val claims = this.getAllClaimsFromToken(token)
            issueAt = claims!!.issuedAt
        } catch (e: Exception) {
            issueAt = null
        }

        return issueAt
    }

    fun getExpirationDateFromToken(token: String): Date? {
        var expiration: Date?
        try {
            val claims = this.getAllClaimsFromToken(token)
            expiration = claims!!.expiration
        } catch (e: Exception) {
            expiration = null
        }

        return expiration
    }


    fun getToken(request: HttpServletRequest): String? {
        val authHeader = getAuthHeaderFromHeader(request)

        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else null

    }

    fun getAuthHeaderFromHeader(request: HttpServletRequest): String? =
        request.getHeader(authHeader)


}
