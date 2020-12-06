package no.eolseng.pg6102.blueprint.config

import no.eolseng.pg6102.blueprint.API_BASE_PATH
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
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
                .antMatchers(HttpMethod.GET, "$API_BASE_PATH/blueprints*/**").permitAll()
                .antMatchers(HttpMethod.HEAD, "$API_BASE_PATH/blueprints*/**").permitAll()
                .antMatchers(HttpMethod.POST, "$API_BASE_PATH/blueprints*/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "$API_BASE_PATH/blueprints*/**").hasRole("ADMIN")
                // Block anything else
                .anyRequest().denyAll()
                .and()
                // Other
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
    }
}