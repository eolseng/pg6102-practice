package no.eolseng.pg6102.coupling.db

import no.eolseng.pg6102.coupling.dto.CouplingDto
import javax.persistence.*

@Entity
@Table(name = "COUPLINGS")
class Coupling(

        @get:Id
        @get:GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @get:ManyToOne
        var user: User,

        @get:ManyToOne
        var blueprint: Blueprint

)

fun Coupling.toDto(): CouplingDto {
    return CouplingDto(id, user.id, blueprint.id)
}