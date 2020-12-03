package no.eolseng.pg6102.blueprint.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                // Exception Handling
                .exceptionHandling().authenticationEntryPoint { req, res, err ->
                    res.setHeader("WWW-Authenticate", "cookie")
                    res.sendError(401)
                }
                .and()
                // Authorization
                .authorizeRequests()
                // Actuator endpoints
                .antMatchers("/actuator/**").permitAll()
                // Service endpoints
                .antMatchers("/api/v1/blueprint**").permitAll()
                // Block anything else
                .anyRequest().denyAll()
                .and()
                // Other
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
    }
}