package no.eolseng.pg6102.blueprint.config

import org.springframework.amqp.core.FanoutExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {
    @Bean
    fun blueprintCreatedFanout(): FanoutExchange {
        return FanoutExchange("blueprint.created.fx")
    }
}