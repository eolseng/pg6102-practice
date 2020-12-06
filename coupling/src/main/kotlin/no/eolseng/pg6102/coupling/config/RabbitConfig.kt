package no.eolseng.pg6102.coupling.config

import org.springframework.amqp.core.FanoutExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {
    @Bean
    fun userCreatedFx(): FanoutExchange {
        return FanoutExchange("user.created.fx")
    }
    @Bean
    fun blueprintCreatedFx(): FanoutExchange {
        return FanoutExchange("blueprint.created.fx")
    }
}