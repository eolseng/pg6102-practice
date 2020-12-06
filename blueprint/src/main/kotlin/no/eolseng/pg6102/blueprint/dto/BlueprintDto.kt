package no.eolseng.pg6102.blueprint.dto

import io.swagger.annotations.ApiModelProperty

data class BlueprintDto(

        @get:ApiModelProperty("The ID of the Blueprint")
        var id: Int? = null,

        @get:ApiModelProperty("The title of the Blueprint")
        var title: String? = null,

        @get:ApiModelProperty("The description of the Blueprint")
        var description: String? = null,

        @get:ApiModelProperty("The value of the Blueprint")
        var value: Int? = null

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
