package no.eolseng.pg6102.blueprint

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.eolseng.pg6102.blueprint.db.BlueprintRepository
import no.eolseng.pg6102.blueprint.db.BlueprintService
import no.eolseng.pg6102.blueprint.db.toDto
import no.eolseng.pg6102.blueprint.dto.BlueprintDto
import no.eolseng.pg6102.blueprint.dto.validateRegistration
import no.eolseng.pg6102.utils.pageination.PageDto
import no.eolseng.pg6102.utils.wrappedresponse.RestResponseFactory
import no.eolseng.pg6102.utils.wrappedresponse.WrappedResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

const val API_BASE_PATH = "/api/v1/blueprint"

@Api(value = API_BASE_PATH, description = "Endpoint managing the Blueprints in the system")
@RestController
@RequestMapping(
        path = [API_BASE_PATH],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
class RestApi(
        private val repository: BlueprintRepository,
        private val service: BlueprintService
) {

    @ApiOperation("Create a new Blueprint")
    @PostMapping
    fun createBlueprint(
            @ApiParam("Data for the new Blueprint")
            @RequestBody dto: BlueprintDto
    ): ResponseEntity<WrappedResponse<Void>> {
        // Validate the DTO
        if (!dto.validateRegistration())
            return RestResponseFactory.userError("Invalid registration data")
        // Save blueprint in the database
        val blueprint = service.createBlueprint(dto.title!!, dto.description!!, dto.value!!)
                ?: return RestResponseFactory.serverFailure("Failed to create Blueprint", 500)
        // Return path to the created blueprint
        return RestResponseFactory.created(URI.create("$API_BASE_PATH/${blueprint.id}"))
    }

    @ApiOperation("Retrieve a specific Blueprint by the ID")
    @GetMapping("/{id}")
    fun getBlueprintById(
            @ApiParam("The id of the Blueprint")
            @PathVariable("id") pathId: String
    ): ResponseEntity<WrappedResponse<Any>> {
        // Convert pathId to Long value
        val id = pathId.toIntOrNull()
                ?: return RestResponseFactory.userError("Id must be a number")
        // Retrieve Blueprint from repository
        val blueprint = repository.findByIdOrNull(id)
                ?: return RestResponseFactory.notFound("Could not find Blueprint with ID $id")
        // Convert to DTO
        val dto = blueprint.toDto()
        // Send the DTO
        return RestResponseFactory.payload(200, dto)
    }

    @ApiOperation("Retrieve all Blueprints")
    @GetMapping
    fun getAllBlueprints(
            @RequestParam("keysetId", required = false)
            keysetId: Int?,
            @RequestParam("keysetTitle", required = false)
            keysetTitle: String?,
            @RequestParam("amount", required = false)
            amount: Int = 10
    ): ResponseEntity<WrappedResponse<PageDto<BlueprintDto>>> {
        // Create page dto
        val page = PageDto<BlueprintDto>()
        // Fetch Blueprints and convert to DTOs
        val dtos = service.getNextPage(amount, keysetId, keysetTitle).map { it.toDto() }
        page.list = dtos
        // Check if not last page
        if (dtos.size == amount) {
            page.next = "$API_BASE_PATH?keysetId=${dtos.last().id}&keysetTitle=${dtos.last().title}&amount=$amount"
        }
        // Return the page
        return RestResponseFactory.payload(200, page)
    }


}