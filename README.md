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
To start all backing services (databases and supporting services), use the script `./start-dev` in the root folder.
### Table of ports
| Name:     | Type:     | Port  |
| ---       | ---       | ---   |
| Gateway   | Service   | 8080  | 
| Auth      | Service   | 8081  |
| Auth      | Database  | 5432  |
| Discovery | Support   | 8500  |
| Redis     | Support   | 6379  |
| RabbitMQ  | Support   | 5672  |
