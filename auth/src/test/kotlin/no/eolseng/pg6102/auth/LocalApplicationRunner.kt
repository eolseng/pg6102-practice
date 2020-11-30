package no.eolseng.pg6102.auth

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(AuthApplication::class.java, "--spring.profiles.active=test")
}