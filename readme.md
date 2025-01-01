# OCP6

---

- [Technologies](#technologies)
- [Run the project using Docker Compose](#run-the-project-using-docker-compose)
- [Run the project manually](#run-the-project-manually)
  - [Setup the database](#setup-the-datase)
  - [Run the back](#run-the-back)
  - [Run the front](#run-the-front)
- [Project folder structure](#project-folder-structure)
  - [Back structure](#back-structure)
  - [Front structure](#front-structure)

---

## Technologies

### Database
* PostgreSQL 15

### Back
* Java 21
* Spring Boot 3
* Maven as project manager

### Front
* Typescript
* Angular 18
* Node and npm
* PrimeNG as UI library

---

## Run the project using Docker Compose

Build the images
```shell
docker compose build
```

Run the containers
```shell
docker compose up -d
```

Then access http://localhost:4200

_The front will be running in prod context_

---

## Run the project manually

### Setup the datase

Install postgres 15 and create a database `ocp6`. Then create an user `ocp6` with the password you want. 
Then, play the sql script located at `/back/src/main/resources/script.sql` in the database to create the tables.

### Run the back

Move to back folder
```shell
cd back
```

Compile the project
```shell
mvn clean compile
```

Run the application (replace the environment variables values with the postgres user's password you just created and a secret key of your choice)
```shell
mvn spring-boot:run -Dspring-boot.run.arguments="--POSTGRES_PASSWORD=yourPassword --SECRET_KEY=yourSecret"
```

The back application will be running on `localhost:8080`

### Run the front

Move to front folder
```shell
cd front
```

Install the dependencies
```shell
npm install
```

Start the application
```shell
ng serve
```

The front application will be running at `localhost:4200`

---

## Project folder structure

### Back structure
The back is following the package by feature code structure.
Thus, packages are based on their feature or functionalities.

- `application` Contains the code to make the application work
  - `authentication` Authentication feature (access and refresh tokens)
  - `errors` Error handling
  - `security` Security of the API
- `domains` Contains business features
  - `comment` Comment feature
  - `post` Post feature
  - `topic` Topic feature
  - `user` User feature

### Front structure

- `app` Application code
  - `core` Essential code used throughout the application
    - `guards`
    - `interceptors`
    - `interface`
    - `services`
  - `features` Feature-based code
  - `shared` Common code
    - `components` 
    - `utilities`
- `environments` Environment variables