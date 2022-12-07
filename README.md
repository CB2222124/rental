# Car Rental Company Console Application

### Project Setup

Clone the repository and open `pom.xml` as a project in your IDE of choice.

### Postgres

Pulls the postgres image from the registry, creates and launches the container.
```
docker pull postgres
docker-compose up -d
```

Stops and removes the container.
```
docker-compose down
```