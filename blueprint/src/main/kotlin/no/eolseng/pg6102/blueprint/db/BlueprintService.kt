package no.eolseng.pg6102.blueprint.db

import no.eolseng.pg6102.blueprint.RestApi
import no.eolseng.pg6102.blueprint.config.blueprintExchangeName
import no.eolseng.pg6102.blueprint.config.newBlueprintRK
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@Service
@Transactional
class BlueprintService(
        private val entityManager: EntityManager,
        private val repo: BlueprintRepository,
        private val rabbit: RabbitTemplate,
        private val blueprintDx: DirectExchange
) {

    private val logger: Logger = LoggerFactory.getLogger(BlueprintService::class.java)

    fun createBlueprint(
            title: String,
            description: String,
            value: Int
    ): Blueprint? {
        // Create the Blueprint
        var blueprint = Blueprint(title = title, description = description, value = value)
        // Save the Blueprint and get the generated ID
        blueprint = repo.save(blueprint)
        // Publish message that new Blueprint is created
        logger.info("Blueprint[ID=${blueprint.id}] sent to RabbitMQ[exchange=${blueprintExchangeName}, routingKey='NEW_BLUEPRINT']")
        rabbit.convertAndSend(blueprintExchangeName, newBlueprintRK, blueprint.id)
        // Return the Blueprint
        return blueprint
    }

    /**
     * Deletes a Blueprint by the given ID
     * @return true if Blueprint
     */
    fun deleteBlueprint(id: Int): Boolean {
        return if (repo.existsById(id)) {
            repo.deleteById(id)
            true
        } else {
            false
        }
    }

    /**
     * Gets all Blueprints sorted by Title. Uses KeySet/Seek pagination
     */
    fun getNextPage(
            keysetId: Int?,
            keysetTitle: String?,
            amount: Int
    ): List<Blueprint> {
        // Query should either have both or none of id and title
        if ((keysetId == null && keysetTitle != null) || (keysetId != null && keysetTitle == null)) {
            throw IllegalArgumentException("Need either both or none of keysetId and keysetTitle")
        }
        // Check if first page
        val firstPage = (keysetId == null && keysetTitle == null)
        // Create query
        val query: TypedQuery<Blueprint>
        if (firstPage) {
            query = entityManager.createQuery(
                    "SELECT b FROM Blueprint b ORDER BY b.title DESC, b.id DESC",
                    Blueprint::class.java)
        } else {
            // Can be used to parameterize the pagination
//            val last = repo.findByIdOrNull(keysetId)
//                    ?: throw IllegalArgumentException("Invalid keysetId - Blueprint does not exist")
//            val ascOrder = false
//            val field = "title"
//
//            val operator = mapOf(Pair(true, ">"), Pair(false, "<"))
//            val sort = mapOf(Pair(true, "ASC"), Pair(false, "DESC"))
//
//            val selectString = "select b from Blueprint b"
//            val whereString = "where b.$field${operator[ascOrder]}?2 or (b.$field=?2 and b.id${operator[ascOrder]}?1)"
//            val orderString = "order by b.$field ${sort[ascOrder]}, b.id ${sort[ascOrder]}"
            query = entityManager.createQuery(
//                    "$selectString $whereString $orderString",
                    "select b from Blueprint b where b.title<?2 or (b.title=?2 and b.id<?1) order by b.title DESC, b.id DESC",
                    Blueprint::class.java)
            query.setParameter(1, keysetId)
            query.setParameter(2, keysetTitle)
//            query.setParameter(1, last.id)
//            query.setParameter(2, last.title)
        }
        query.maxResults = amount
        return query.resultList
    }

}