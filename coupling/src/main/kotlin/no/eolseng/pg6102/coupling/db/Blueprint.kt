package no.eolseng.pg6102.coupling.db

import javax.persistence.*

@Entity
@Table(name = "BLUEPRINTS")
class Blueprint(

        @get:Id
        var id: Long,

        @get:OneToMany(mappedBy = "blueprint", cascade = [(CascadeType.ALL)])
        var couplings: MutableList<Coupling> = mutableListOf()

)