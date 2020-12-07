package no.eolseng.pg6102.coupling.db

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "USERS")
class User(

        @get:Id
        @get:NotNull
        var id: String? = null,

        @get:OneToMany(mappedBy = "user", cascade = [(CascadeType.ALL)])
        var couplings: MutableList<Coupling> = mutableListOf()

)