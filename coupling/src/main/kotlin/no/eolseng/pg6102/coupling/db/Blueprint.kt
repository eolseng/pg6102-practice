package no.eolseng.pg6102.coupling.db

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "BLUEPRINTS")
class Blueprint(

        @get:Id
        @get:NotNull
        var id: Long? = null,

        @get:OneToMany(mappedBy = "blueprint", cascade = [(CascadeType.ALL)])
        var couplings: MutableList<Coupling> = mutableListOf()

)