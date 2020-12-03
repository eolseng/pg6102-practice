package no.eolseng.pg6102.blueprint.db

import org.springframework.data.jpa.repository.JpaRepository

interface BlueprintRepository : JpaRepository<Blueprint, Int> {

    fun existsByTitle(title: String): Boolean

}