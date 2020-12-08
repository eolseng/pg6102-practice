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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.net.URI

const val API_BASE_PATH = "/api/v1/blueprint"

@Api(value = "$API_BASE_PATH/blueprints", description = "Endpoint managing the Blueprints in the system")
@RestController
@RequestMapping(
        path = ["$API_BASE_PATH/blueprints"],
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
        // Check if title exists
        if (repository.existsByTitle(dto.title!!))
            return RestResponseFactory.userError("Blueprint with title ${dto.title} already exists")
        // Save Blueprint in the database
        val blueprint = service.createBlueprint(dto.title!!, dto.description!!, dto.value!!)
                ?: return RestResponseFactory.serverFailure("Failed to create Blueprint", 500)
        // Return path to the created Blueprint
        return RestResponseFactory.created(URI.create("$API_BASE_PATH/blueprints/${blueprint.id}"))
    }

    @ApiOperation("Delete a specific Blueprint by the ID")
    @DeleteMapping("/{id}")
    fun deleteBlueprintById(
            @ApiParam("The ID of the Blueprint to delete")
            @PathVariable("id") pathId: String,
            auth: Authentication
    ): ResponseEntity<WrappedResponse<Void>> {
        // Convert pathId to Int value
        val id = pathId.toIntOrNull()
                ?: return RestResponseFactory.userError("ID must be a number")
        // Check that the Blueprint exists
        if (!repository.existsById(id))
            return RestResponseFactory.userError("Blueprint with ID $id does not exist")
        // Delete the Blueprint
        repository.deleteById(id)
        // Return confirmation to user
        return RestResponseFactory.noPayload(204)
    }

    @ApiOperation("Retrieve a specific Blueprint by the ID")
    @GetMapping("/{id}")
    fun getBlueprintById(
            @ApiParam("The ID of the Blueprint to retrieve")
            @PathVariable("id") pathId: String
    ): ResponseEntity<WrappedResponse<Any>> {
        // Convert pathId to Int value
        val id = pathId.toIntOrNull()
                ?: return RestResponseFactory.userError("ID must be a number")
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
            amount: Int?
    ): ResponseEntity<WrappedResponse<PageDto<BlueprintDto>>> {
        // Set amount if not supplied
        val amount = amount ?: 10
        // Create page dto
        val page = PageDto<BlueprintDto>()
        // Fetch Blueprints and convert to DTOs
        val dtos = service.getNextPage(keysetId, keysetTitle, amount).map { it.toDto() }
        page.list = dtos
        // Check if not last page - will return a blank last page if match
        if (dtos.size == amount) {
            page.next = "$API_BASE_PATH/blueprints?keysetId=${dtos.last().id}&keysetTitle=${dtos.last().title}&amount=$amount"
        }
        // Return the page
        return RestResponseFactory.payload(200, page)
    }

}