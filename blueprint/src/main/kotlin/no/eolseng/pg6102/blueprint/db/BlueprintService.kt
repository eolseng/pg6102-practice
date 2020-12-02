package no.eolseng.pg6102.blueprint.db

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@Service
class BlueprintService(
        private val entityManager: EntityManager,
        private val repo: BlueprintRepository,
        private val rabbit: RabbitTemplate,
        private val blueprintCreatedFx: FanoutExchange
) {

    fun createBlueprint(
            title: String,
            description: String,
            value: Int
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

    fun getNextPage(
            amount: Int,
            keysetId: Int?,
            keysetTitle: String?
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
                    Blueprint::class.java
            )
        } else {
            query = entityManager.createQuery(
                    "select b from Blueprint b where b.title<?2 or (b.title=?2 and b.id<?1) order by b.title DESC, b.id DESC",
                    Blueprint::class.java
            )
            query.setParameter(1, keysetId)
            query.setParameter(2, keysetTitle)
        }
        query.maxResults = amount

        return query.resultList

    }

}