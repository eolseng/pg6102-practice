package no.eolseng.pg6102.coupling.config

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CircuitBreakerConfig {

    @Bean
    fun cbFactory(): Resilience4JCircuitBreakerFactory {
        return Resilience4JCircuitBreakerFactory()
    }

    @Bean
    fun blueprintCircuitBreaker(): CircuitBreaker {
        return cbFactory().create("blueprintCircuitBreaker")
    }

}