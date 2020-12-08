package no.eolseng.pg6102.blueprint.config

import org.springframework.amqp.core.DirectExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val blueprintExchangeName = "pg6102.blueprint.dx"
const val newBlueprintRK = "new_blueprint"

@Configuration
class RabbitConfig {

    @Bean
    fun blueprintDx(): DirectExchange {
        return DirectExchange(blueprintExchangeName)
    }

}