package no.eolseng.pg6102.coupling.db

import javax.persistence.*

@Entity
class Coupling(

        @get:Id
        @get:GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @get:ManyToOne
        var user: User,

        @get:ManyToOne
        var blueprint: Blueprint

)