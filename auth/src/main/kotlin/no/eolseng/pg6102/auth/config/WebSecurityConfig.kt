package no.eolseng.pg6102.auth.config

import no.eolseng.pg6102.auth.db.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
        private val userDetailsService: UserDetailsServiceImpl
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http
                // Exception Handling
                .exceptionHandling().authenticationEntryPoint { req, res, err ->
                    res.setHeader("WWW-Authenticate", "cookie")
                    res.sendError(401)
                }
                .and()

                // Logout
                .logout().logoutUrl("/api/v1/auth/logout")
                .logoutSuccessHandler((HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)))
                .and()

                // Authorization
                .authorizeRequests()
                    // Actuator endpoints
                .antMatchers("/actuator/**").permitAll()
                    // Service End-Points
                .antMatchers("/api/v1/auth/signup").permitAll()
                .antMatchers("/api/v1/auth/login").permitAll()
                .antMatchers("/api/v1/auth/logout").permitAll()
                .antMatchers("/api/v1/auth/user").authenticated()
                    // Block anything else
                .anyRequest().denyAll()
                .and()

                // Other
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    }
}