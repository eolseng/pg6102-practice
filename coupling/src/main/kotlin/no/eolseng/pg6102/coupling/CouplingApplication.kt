package no.eolseng.pg6102.coupling

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class CouplingApplication

fun main(args: Array<String>) {
    SpringApplication.run(CouplingApplication::class.java, *args)
}