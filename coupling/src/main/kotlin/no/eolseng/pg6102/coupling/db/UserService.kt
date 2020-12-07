package no.eolseng.pg6102.coupling.db

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
        private val repo: UserRepository
) {

    fun getUserById(id: String): User {
        // TODO: Create Exists-check for username on Auth Service
        // This is currently safe as RestApi checks that the auth.name and dto.userId is the same
        return repo.findByIdOrNull(id) ?: createUser(id)
    }

    fun createUser(id: String): User {
        return repo.save(User(id = id))
    }

}