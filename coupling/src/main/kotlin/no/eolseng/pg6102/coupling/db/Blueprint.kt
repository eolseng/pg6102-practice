package no.eolseng.pg6102.coupling.db

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Blueprint(

        @get:Id
        var id: Long,

        @get:OneToMany(mappedBy = "blueprint", cascade = [(CascadeType.ALL)])
        var couplings: MutableList<Coupling> = mutableListOf()

)