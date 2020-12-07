package no.eolseng.pg6102.coupling

import io.swagger.annotations.Api
import no.eolseng.pg6102.coupling.db.BlueprintService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val API_BASE_PATH = "/api/v1/coupling"

@Api(value = API_BASE_PATH, description = "Endpoint managing the Couplings in the system")
@RestController
@RequestMapping(
        path = ["$API_BASE_PATH/couplings"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
class RestApi(
        private val blueprintService: BlueprintService
) {

    @GetMapping
    fun checkCB() {
        if (blueprintService.getBlueprint(1L) != null){
            println("JA TAKK")
        } else {
            println("NEI TAKK")
        }
    }

}