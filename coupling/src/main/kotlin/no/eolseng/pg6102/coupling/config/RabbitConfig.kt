package no.eolseng.pg6102.coupling.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// Blueprint
const val blueprintDxName = "pg6102.blueprint.dx"
const val newBlueprintQueueName = "new-blueprint.couplings.q"
const val newBlueprintRK = "new_blueprint"

@Configuration
class RabbitConfig {

    // BLUEPRINT CREATED
    @Bean
    fun blueprintDx(): DirectExchange {
        return DirectExchange(blueprintDxName)
    }

    @Bean
    fun newBlueprintQueue(): Queue {
        return Queue(newBlueprintQueueName)
    }

    @Bean
    fun newBlueprintBinding(
            blueprintDx: DirectExchange,
            newBlueprintQueue: Queue
    ): Binding {
        return BindingBuilder
                .bind(newBlueprintQueue)
                .to(blueprintDx)
                .with(newBlueprintRK)
    }

}