package no.eolseng.pg6102.blueprint

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class BlueprintApplication

fun main(args: Array<String>) {
    SpringApplication.run(BlueprintApplication::class.java, *args)
}