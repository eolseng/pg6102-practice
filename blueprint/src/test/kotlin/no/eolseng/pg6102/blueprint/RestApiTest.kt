package no.eolseng.pg6102.blueprint

import io.restassured.RestAssured
import io.restassured.http.ContentType
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import no.eolseng.pg6102.blueprint.db.BlueprintRepository
import no.eolseng.pg6102.blueprint.db.BlueprintService
import no.eolseng.pg6102.blueprint.dto.BlueprintDto
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(
        classes = [(BlueprintApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RestApiTest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    lateinit var repository: BlueprintRepository

    @Autowired
    lateinit var service: BlueprintService

    @BeforeEach
    @AfterEach
    fun setup() {
        // Setup RestAssured
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "$API_BASE_PATH/"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        // Clear repository
        repository.deleteAll()
    }

    companion object {
        // Generates unique IDs
        private var idCounter = 0
        fun getId(): Int {
            return idCounter++
        }
    }

    /**
     * Registers a new Blueprint with the service.
     * Generates a unique title and description - not coupled with the returned ID.
     *
     * @param value (optional) of the created blueprint
     * @return ID of newly created blueprint
     */
    fun registerBlueprint(value: Int = 0): Int {
        // Generate unique title and description
        val id = getId()
        val title = "test_title_$id"
        val description = "test_description_$id"
        // Create the DTO
        val dto = BlueprintDto(title = title, description = description, value = value.toLong())
        // Register the Blueprint and extract the Location Header
        val redirect =
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(dto)
                        .post("/")
                        .then().assertThat()
                        .statusCode(201)
                        .extract()
                        .header("Location")
        // Extract the ID from the Location Header and return it
        return redirect.substringAfter(RestAssured.basePath).toInt()
    }

    @Test
    fun `register blueprint`() {
        val id = registerBlueprint()
        val blueprint = repository.findById(id)
        assertTrue(blueprint.isPresent)
    }

    @Test
    fun `retrieve blueprint by id`() {
        val value = 200
        val id = registerBlueprint(value)
        val blueprint = repository.findById(id).get()

        RestAssured.given()
                .accept(ContentType.JSON)
                .get("/$id")
                .then().assertThat()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .body("data.title", equalTo(blueprint.title))
                .body("data.description", equalTo(blueprint.description))
                .body("data.value", equalTo(value))
    }

}