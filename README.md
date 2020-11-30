# Practise project for PG6102
Microservice architecture based on SpringBoot, written in Kotlin.

Attempting to design as a "Twelve-Factor App", but using a root-POM as done in the course.

## Scripts:
* `./build-and-start`
    * Rebuilds all .jar files and Docker images
    * Starts the project locally
* `./start-dev`
    * Used for local development
    * Starts all supporting services as Docker images
> Use 'chmod +x ./**SCRIPT_NAME**' if permission is denied.

## Local development
Use the script `./start-dev` in the root folder to start all backing services (databases, Redis etc.).

## Modules
### Gateway
Used as the entrypoint for the microservices. Exposed on port 8080 and redirects requests to the correct service based on URL paths.

### Auth
Authentication service that stores usernames and hashed passwords in the connected database.
Uses Redis to store active sessions so other services can verify requests that require authentication,
and messages the other services via RabbitMQ on creation of new accounts.



## Supporting services
* **PostgreSQL** database for persistent storage.
* **Consul** as discovery service.
* **Redis** for storing active sessions.
* **RabbitMQ** as message-broker.

### Table of ports
| Name:     | Type:     | Port  |
| ---       | ---       | ---   |
| Gateway   | Service   | 8080  | 
| Auth      | Service   | 8081  |
| Auth      | Database  | 5432  |
| Discovery | Support   | 8500  |
| Redis     | Support   | 6379  |
| RabbitMQ  | Support   | 5672  |
