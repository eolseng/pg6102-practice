package no.eolseng.pg6102.auth

import no.eolseng.pg6102.auth.db.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

const val ENV_VARIABLE_ADMIN_USERNAME = "AUTH_ADMIN_USERNAME"
const val ENV_VARIABLE_ADMIN_PASSWORD = "AUTH_ADMIN_PASSWORD"

const val DEFAULT_ADMIN_USERNAME = "admin"
const val DEFAULT_ADMIN_PASSWORD = "admin"

@Component
class InitialDataLoader(
        private val userService: UserService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        createAdminUser()
    }

    /**
     * Creates a Admin User for the service based on Environment Variables
     */
    private fun createAdminUser() {
        val username = getEnvVariableOrDefault(
                envVariable = ENV_VARIABLE_ADMIN_USERNAME,
                default = DEFAULT_ADMIN_USERNAME)
        val password = getEnvVariableOrDefault(
                envVariable = ENV_VARIABLE_ADMIN_PASSWORD,
                default = DEFAULT_ADMIN_PASSWORD)
        userService.createUser(username, password, setOf("USER", "ADMIN"))
    }

    private fun getEnvVariableOrDefault(envVariable: String, default: String): String {
        return (System.getenv(envVariable) ?: default).trim()
    }

}