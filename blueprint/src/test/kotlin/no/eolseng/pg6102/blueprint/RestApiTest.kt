package no.eolseng.pg6102.blueprint

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import no.eolseng.pg6102.blueprint.db.BlueprintRepository
import no.eolseng.pg6102.blueprint.dto.BlueprintDto
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
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

    @BeforeEach
    @AfterEach
    fun setup() {
        // Setup RestAssured
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "$API_BASE_PATH/blueprints"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        // Clear repository
        repository.deleteAll()
    }

    companion object {
        // Generates unique IDs
        private var idCounter = 1
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
        val dto = BlueprintDto(title = title, description = description, value = value)
        // Register the Blueprint and extract the Location Header
        val redirect =
                RestAssured.given()
                        .auth().basic("admin", "admin")
                        .contentType(ContentType.JSON)
                        .body(dto)
                        .post()
                        .then().assertThat()
                        .statusCode(201)
                        .extract()
                        .header("Location")
        // Extract the ID from the Location Header and return it
        return redirect.substringAfter("${RestAssured.basePath}/").toInt()
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

    @Test
    fun `delete blueprint by id`() {
        val id = registerBlueprint()

        // Not ADMIN
        RestAssured.given()
                .accept(ContentType.JSON)
                .delete("/$id")
                .then().assertThat()
                .statusCode(401)
        // Is ADMIN
        RestAssured.given()
                .auth().basic("admin", "admin")
                .accept(ContentType.JSON)
                .delete("/$id")
                .then().assertThat()
                .statusCode(204)
    }

    @Test
    fun `get all blueprints - one page`() {
        val amount = 10
        // Register n Blueprints
        for (x in 0 until amount) {
            registerBlueprint()
        }
        // Verify with repository
        assertEquals(amount, repository.count().toInt())

        RestAssured.given()
                .accept(ContentType.JSON)
                .get("?amount=${amount + 1}")
                .then().assertThat()
                .statusCode(200)
                .body("data.list.size()", equalTo(amount))
                .body("data.next", nullValue())
    }

    fun Response.getBlueprints(): List<BlueprintDto> {
        return this.jsonPath().getList("data.list", BlueprintDto::class.java)
    }

    fun Response.getNextLink(): String? {
        return this.jsonPath().get("data.next")
    }

    fun List<BlueprintDto>.checkOrder() {
        for (i in 0 until this.size - 1) {
            assertTrue(this[i].title!! >= this[i + 1].title!!)
            if (this[i].title!! == this[i + 1].title!!) {
                assertTrue(this[i].id!! >= this[i + 1].id!!)
            }
        }
    }

    @Test
    fun `get all blueprints - multiple pages`() {
        val pages = 4
        val pageSize = 5
        val total = pages * pageSize
        val uniqueIds = mutableSetOf<Int>()

        // Register n Blueprints
        for (x in 0 until total) {
            registerBlueprint()
        }
        // Verify with repository
        assertEquals(total, repository.count().toInt())
        // Get first page
        var res = RestAssured.given()
                .accept(ContentType.JSON)
                .get("?amount=$pageSize")
                .then().assertThat()
                .statusCode(200)
                .body("data.list.size()", equalTo(pageSize))
                .body("data.next", anything())
                .extract()
                .response()
        var dtos = res.getBlueprints()
        var next = res.getNextLink()
        // Add IDs to unique ID list
        uniqueIds.addAll(dtos.map { it.id!! })
        // Check the ordering
        dtos.checkOrder()
        // Check rest of the pages
        while (next != null) {
            res = RestAssured.given()
                    .accept(ContentType.JSON)
                    .basePath("")
                    .get(next)
                    .then().assertThat()
                    .statusCode(200)
                    .extract()
                    .response()
            dtos = res.getBlueprints()
            next = res.getNextLink()
            // Add IDs to unique ID list
            uniqueIds.addAll(dtos.map { it.id!! })
            // Check the ordering
            dtos.checkOrder()
        }
        // Check that all Blueprints have been retrieved
        assertEquals(total, uniqueIds.size)
    }

}