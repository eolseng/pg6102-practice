package no.eolseng.pg6102.blueprint.db

import org.springframework.amqp.AmqpException
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class BlueprintService(
        private val repo: BlueprintRepository,
        private val rabbit: RabbitTemplate,
        private val blueprintCreatedFx: FanoutExchange
) {

    fun createBlueprint(
            title: String,
            description: String,
            value: Long
    ): Blueprint? {

        // Create the blueprint
        var blueprint = Blueprint(title = title, description = description, value = value)

        // Save the blueprint and get the generated ID
        blueprint = repo.save(blueprint)

        // Publish message that new blueprint is created
        rabbit.convertAndSend(blueprintCreatedFx.name, "", blueprint.id)

        // Return the blueprint
        return blueprint

    }

}