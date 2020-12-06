package no.eolseng.pg6102.coupling

import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val API_BASE_PATH = "/api/v1/coupling"

@Api(value = API_BASE_PATH, description = "Endpoint managing the Couplings in the system")
@RestController
@RequestMapping(
        path = [API_BASE_PATH],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
class RestApi {



}