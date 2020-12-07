package no.eolseng.pg6102.coupling.config

import no.eolseng.pg6102.coupling.API_BASE_PATH
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
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
                .antMatchers("$API_BASE_PATH/couplings*/**").authenticated()
                // Block anything else
                .anyRequest().denyAll()
                .and()
                // Other
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
    }
}