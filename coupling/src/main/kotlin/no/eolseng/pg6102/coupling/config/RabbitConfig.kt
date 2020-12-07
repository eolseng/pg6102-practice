package no.eolseng.pg6102.coupling.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// User
const val userCreatedExchangeName = "user.created.fx"
const val userCreatedQueueName = "create-user_couplings_q"

// Blueprint
const val blueprintCreatedExchangeName = "blueprint.created.fx"
const val blueprintCreatedQueueName = "create-blueprint_couplings_q"

@Configuration
class RabbitConfig {

    // USER CREATED
    @Bean
    fun userCreatedFx(): FanoutExchange {
        return FanoutExchange(userCreatedExchangeName)
    }

    @Bean
    fun createUserQueue(): Queue {
        return Queue(userCreatedQueueName)
    }

    @Bean
    fun createUserBinding(
            userCreatedFx: FanoutExchange,
            createUserQueue: Queue
    ): Binding {
        return BindingBuilder
                .bind(createUserQueue)
                .to(userCreatedFx)
    }

    @RabbitListener(queues = [userCreatedQueueName])
    fun createUserOnMessage(username: String) {
        // Todo: Not yet implemented
    }

    // BLUEPRINT CREATED
    @Bean
    fun blueprintCreatedFx(): FanoutExchange {
        return FanoutExchange(blueprintCreatedExchangeName)
    }

    @Bean
    fun createBlueprintQueue(): Queue {
        return Queue(blueprintCreatedQueueName)
    }

    @Bean
    fun createBlueprintBinding(
            blueprintCreatedFx: FanoutExchange,
            createBlueprintQueue: Queue
    ): Binding {
        return BindingBuilder
                .bind(createBlueprintQueue)
                .to(blueprintCreatedFx)
    }

    @RabbitListener(queues = [blueprintCreatedQueueName])
    fun createBlueprintOnMessage(id: Int) {
        // Todo: Not yet implemented
    }

}