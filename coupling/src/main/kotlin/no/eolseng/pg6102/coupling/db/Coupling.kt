package no.eolseng.pg6102.coupling.db

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "COUPLINGS")
class Coupling(

        @get:Id
        @get:GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @get:NotNull
        @get:ManyToOne
        var user: User,

        @get:NotNull
        @get:ManyToOne
        var blueprint: Blueprint

)

//fun Coupling.toDto(): CouplingDto {
//    return CouplingDto(id, user.id, blueprint.id)
//}