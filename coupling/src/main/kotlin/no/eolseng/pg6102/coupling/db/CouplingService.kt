package no.eolseng.pg6102.coupling.db

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CouplingService(
        private val repo: CouplingRepository
) {

    fun createCoupling(
            user: User,
            blueprint: Blueprint
    ): Coupling? {
        // Create the Coupling
        var coupling = Coupling(user = user, blueprint = blueprint)
        // Save the Coupling and get the generated ID
        coupling = repo.save(coupling)
        // Return the Coupling
        return coupling
    }

}