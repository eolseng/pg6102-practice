package no.eolseng.pg6102.auth.config

import org.springframework.amqp.core.FanoutExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {
    @Bean
    fun userCreatedFanout(): FanoutExchange {
        return FanoutExchange("user.created.fx")
    }
}