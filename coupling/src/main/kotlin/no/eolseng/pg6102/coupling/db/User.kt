package no.eolseng.pg6102.coupling.db

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class User(

        @get:Id
        var id: Long,

        @get:OneToMany(mappedBy = "user", cascade = [(CascadeType.ALL)])
        var couplings: MutableList<Coupling> = mutableListOf()
)