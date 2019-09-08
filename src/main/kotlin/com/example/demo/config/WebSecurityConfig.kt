package com.example.demo.config


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import com.example.demo.security.TokenUtils
import com.example.demo.security.auth.RestAuthenticationEntryPoint
import com.example.demo.security.auth.TokenAuthenticationFilter
import com.example.demo.service.UserService

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtUserDetailsService: UserService

    @Autowired
    private lateinit var restAuthenticationEntryPoint: RestAuthenticationEntryPoint

    @Autowired
    private lateinit var tokenUtils: TokenUtils

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http.csrf().disable()

        http.cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET).permitAll()
            .antMatchers(HttpMethod.POST).permitAll()
            .antMatchers().permitAll()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/users/registerUser").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated().and()
            .addFilterBefore(TokenAuthenticationFilter(tokenUtils, jwtUserDetailsService), BasicAuthenticationFilter::class.java)

    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(HttpMethod.POST, "/api/auth/**")
        web.ignoring().antMatchers(HttpMethod.PUT, "/api/auth/**")
        web.ignoring().antMatchers(HttpMethod.POST, "/api/users/registerUser")
        web.ignoring().antMatchers(HttpMethod.PUT, "/api/users/registerUser")
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js")
    }

}
