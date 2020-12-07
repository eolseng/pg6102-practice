package no.eolseng.pg6102.coupling.db

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
@Transactional
class UserService(
        private val repo: UserRepository
) {

    fun getUserById(id: String): User {
        return repo.findByIdOrNull(id) ?: createUser(id)
    }

    fun createUser(id: String): User {
        return repo.save(User(id = id))
    }

}