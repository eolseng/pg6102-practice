package no.eolseng.pg6102.coupling.db

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>