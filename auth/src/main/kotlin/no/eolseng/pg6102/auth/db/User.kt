package no.eolseng.pg6102.auth.db

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "USER")
class User (

        @Id
        @get:NotBlank
        var username: String,

        @get:NotBlank
        var password: String,

        @ElementCollection
        @CollectionTable(
                name = "authorities",
                joinColumns = [JoinColumn(name = "username")]
        )
        @get:Column(name = "authority")
        @get:NotNull
        var roles: Set<String> = setOf(),

        @get:NotNull
        var enabled: Boolean = true

)