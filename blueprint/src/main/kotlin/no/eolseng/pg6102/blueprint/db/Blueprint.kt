package no.eolseng.pg6102.blueprint.db

import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "BLUEPRINTS")
class Blueprint {

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @get:NotBlank(message = "Blueprint must have title")
    var title: String = ""

    @get:NotBlank(message = "Blueprint must have description")
    var description: String = ""

    @get:NotNull
    @get:Min(0, message = "Value cannot be negative")
    @get:Max(Long.MAX_VALUE, message = "Value cannot be greater than ${Long.MAX_VALUE}")
    var value: Long = 0

}