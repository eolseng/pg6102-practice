package no.eolseng.pg6102.blueprint.db

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

@Service
class BlueprintService(
        private val entityManager: EntityManager,
        private val repo: BlueprintRepository,
        private val rabbit: RabbitTemplate,
        private val blueprintCreatedFx: FanoutExchange
) {

    @Transactional
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

    /**
     * Deletes a Blueprint by the given ID
     * @return true if Blueprint
     */
    @Transactional
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
    @Transactional
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