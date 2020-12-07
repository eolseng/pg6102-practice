package no.eolseng.pg6102.blueprint

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(BlueprintApplication::class.java, "--spring.profiles.active=dev")
}