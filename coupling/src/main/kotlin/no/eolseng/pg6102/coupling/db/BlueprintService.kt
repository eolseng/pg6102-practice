package no.eolseng.pg6102.coupling.db


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
@Transactional
class BlueprintService(
        private val repo: BlueprintRepository,
        private val client: RestTemplate,
        @Qualifier("blueprintCircuitBreaker")
        private val cb: CircuitBreaker
) {

    @Value("\${service_urls.blueprint}")
    private lateinit var blueprintUrl: String

    val logger: Logger = LoggerFactory.getLogger(BlueprintService::class.java)

    fun getBlueprint(id: Long): Blueprint? {
        // Check if Blueprint exists in local DB
        return repo.findByIdOrNull(id)
        // Not in DB - check with Blueprint Service
                ?: if (verifyBlueprint(id)) {
                    // Blueprint exists in Blueprint Service - create and return local copy
                    createBlueprint(id)
                } else {
                    // Blueprint does not exists - return null
                    null
                }
    }

    fun createBlueprint(id: Long): Blueprint {
        return repo.save(Blueprint(id = id))
    }

    private fun verifyBlueprint(id: Long): Boolean {
        return cb.run(
                {
                    val uri = URI("http://$blueprintUrl/api/v1/blueprint/blueprints/$id")
                    try {
                        // Use HEAD request to check with Blueprint Service if Blueprint exists
                        client
                                .exchange(uri, HttpMethod.HEAD, null, String::class.java)
                                .statusCode.is2xxSuccessful
                    } catch (err: HttpClientErrorException.NotFound) {
                        // Catch 404 as an expected error
                        false
                    }
                },
                { err ->
                    // Client did not return 2xx or 404
                    logger.error("Failed to retrieve data from Blueprint Service: ${err.message}")
                    false
                })
    }
}