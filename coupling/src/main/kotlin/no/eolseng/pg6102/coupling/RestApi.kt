package no.eolseng.pg6102.coupling

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.eolseng.pg6102.coupling.db.*
import no.eolseng.pg6102.coupling.dto.CouplingDto
import no.eolseng.pg6102.coupling.dto.validateRegistration
import no.eolseng.pg6102.utils.wrappedresponse.RestResponseFactory
import no.eolseng.pg6102.utils.wrappedresponse.WrappedResponse
import org.apache.naming.factory.ResourceFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.net.URI

const val API_BASE_PATH = "/api/v1/coupling"

@Api(value = API_BASE_PATH, description = "Endpoint managing the Couplings in the system")
@RestController
@RequestMapping(
        path = ["$API_BASE_PATH/couplings"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
class RestApi(
        private val repository: CouplingRepository,
        private val couplingService: CouplingService,
        private val userService: UserService,
        private val blueprintService: BlueprintService
) {

    @ApiOperation("Create a Coupling between User and Blueprint")
    @PostMapping
    fun createCoupling(
            @ApiParam("Data of the new Coupling")
            @RequestBody dto: CouplingDto,
            auth: Authentication
    ): ResponseEntity<WrappedResponse<Void>> {
        // Validate DTO
        if (!dto.validateRegistration())
            return RestResponseFactory.userError("Invalid registration data")
        // Only allow user to register coupling on self
        if (dto.userId != auth.name)
            return RestResponseFactory.userError(httpStatusCode = 403, message = "Cannot register Coupling on other users")
        // Get User if exists - will always exists because of prev. check
        val user = userService.getUserById(dto.userId!!)
        // Get Blueprint if exists
        val blueprint = blueprintService.getBlueprint(dto.blueprintId!!)
                ?: return RestResponseFactory.notFound("No Blueprint with ID ${dto.blueprintId} exists")
        // Create Coupling
        val coupling = couplingService.createCoupling(user, blueprint)
                ?: return RestResponseFactory.serverFailure("Failed to create Coupling")
        // Return path to the created Coupling
        return RestResponseFactory.created(URI.create("$API_BASE_PATH/couplings/${coupling.id}"))
    }

    @ApiOperation("Retrieve a specific Coupling by the ID")
    @GetMapping("/{id}")
    fun getCouplingById(
            @ApiParam("The ID of the Coupling to retrieve")
            @PathVariable("id") pathId: String
    ): ResponseEntity<WrappedResponse<Any>> {
        // TODO: Må sjekke med Auth!
        // Convert pathId to Long value
        val id = pathId.toLongOrNull()
                ?: return RestResponseFactory.userError("ID must be a number")
        // Retrieve the Coupling from the repository
        val coupling = repository.findByIdOrNull(id)
                ?: return RestResponseFactory.notFound("Could not find Coupling with ID $id")
        // Convert to DTO
        val dto = coupling.toDto()
        // Send the DTO
        return RestResponseFactory.payload(200, dto)
    }

}