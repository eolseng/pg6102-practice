package no.eolseng.pg6102.coupling.db

import javax.persistence.*

@Entity
@Table(name = "USERS")
class User(

        @get:Id
        var id: Long
//
//        @get:OneToMany(mappedBy = "user", cascade = [(CascadeType.ALL)])
//        var couplings: MutableList<Coupling> = mutableListOf()
)