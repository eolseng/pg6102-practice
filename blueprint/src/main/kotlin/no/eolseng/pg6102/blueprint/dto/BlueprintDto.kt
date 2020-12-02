package no.eolseng.pg6102.blueprint.dto

import io.swagger.annotations.ApiModelProperty

data class BlueprintDto(

        @get:ApiModelProperty("The ID of the blueprint")
        var id: Int? = null,

        @get:ApiModelProperty("The title of the blueprint")
        var title: String? = null,

        @get:ApiModelProperty("The description of the blueprint")
        var description: String? = null,

        @get:ApiModelProperty("The value of the blueprint")
        var value: Long? = null

)

fun BlueprintDto.validateRegistration(): Boolean {

    // Check for null
    val title = this.title ?: return false
    val description = this.description ?: return false
    val value = this.value ?: return false

    // Other validations
    if (title.isEmpty()) return false
    if (description.isEmpty()) return false
    if (value < 0) return false

    // Dto is valid for registration
    return true

}
