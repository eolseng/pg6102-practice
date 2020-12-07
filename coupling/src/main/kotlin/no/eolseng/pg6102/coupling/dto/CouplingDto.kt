package no.eolseng.pg6102.coupling.dto

import io.swagger.annotations.ApiModelProperty

data class CouplingDto(

        @get:ApiModelProperty("The ID of the Coupling")
        var id: Long? = null,

        @get:ApiModelProperty("The ID of the Couplings User")
        var userId: String? = null,

        @get:ApiModelProperty("The ID of the Couplings Blueprint")
        var blueprintId: Long? = null

)
