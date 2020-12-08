package no.eolseng.pg6102.coupling

import no.eolseng.pg6102.coupling.config.newBlueprintQueueName
import no.eolseng.pg6102.coupling.db.BlueprintService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.web.bind.annotation.RestController

@RestController
class MoMListener(
        private val blueprintService: BlueprintService
) {

    val logger: Logger = LoggerFactory.getLogger(MoMListener::class.java)

    @RabbitListener(queues = [newBlueprintQueueName])
    fun createBlueprintOnMessage(id: Int) {
        logger.info("New Blueprint[id=$id] from RabbitMQ[queue=$newBlueprintQueueName]")
        blueprintService.createBlueprint(id.toLong())
    }

}