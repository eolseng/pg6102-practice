package no.eolseng.pg6102.coupling

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(CouplingApplication::class.java, "--spring.profiles.active=dev")
}