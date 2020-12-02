package no.eolseng.pg6102.blueprint.db

import no.eolseng.pg6102.blueprint.dto.BlueprintDto
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "BLUEPRINTS")
class Blueprint(

        @get:Id
        @get:GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @get:Column(unique = true)
        @get:NotBlank(message = "Blueprint must have title")
        var title: String = "",

        @get:NotBlank(message = "Blueprint must have description")
        var description: String = "",

        @get:NotNull
        @get:Min(0, message = "Value cannot be negative")
        @get:Max(Int.MAX_VALUE.toLong(), message = "Value cannot be greater than ${Int.MAX_VALUE}")
        var value: Int = 0

)

fun Blueprint.toDto(): BlueprintDto {
    return BlueprintDto(id, title, description, value)
}