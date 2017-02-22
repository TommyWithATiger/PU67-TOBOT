# TOBOT

## Development environment

### Tools to get started

You will need:
 - [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
 - [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - [Node.js](https://nodejs.org/en/)

### SetUp for dev environment

You can use make for all steps.

#### Backend (`make backend`)

```bash
$ cd java # Go into the server section.
$ mvn clean install # Compile code.
$ mvn exec:java -D exec.mainClass=main.ServerInitializer # Run server.
```

#### Frontend (`make frontend`)

```bash
$ cd web # Go into the web section.
$ npm install # Install frontend dependecies.
$ npm run dev # This will automatically run a dev server with hot-reload.
```

## Production environment (`make`)

These commands will run the whole application on port 5032.

```bash
# First compile the frontend.
$ cd web # Go into the web section.
$ npm install # Install frontend dependecies.
$ npm run build # Install frontend dependecies.
$ cd .. # Go back to root folder.

# Finish by running the server.
$ cd java # Go into the server section.
$ mvn clean install # Compile code.
$ mvn exec:java -D exec.mainClass=main.ServerInitializer # Run server.
```

You can also just use make

```bash
$ make # Building frontend and backend and running server.
```

## Production

We recommend using docker as production environment as it has low overhead
and ensures security and portability.

We have 3 parts of the production environment:
 1. Build a **base image**.
 2. Build a **production image** based on the **base image**.
 3. Create a **production container** based on the **production image**.

This makes the second image faster to create as it already has all the masjor dependencies.

### 1. Build base image

```bash
$ make docker-baseimage
```

### 2. Build production image from the base image

```bash
$ make docker-image-prod
```

### 3. Create production container running on port 8081

```bash
$ make docker-container-prod
```

These make commands will handle all the work with recreation of containers.
But feel free to use plain docker or docker-compose.

### Entering the production container

Sometimes you need to debug a container. You can enter it with:

```bash
$ make docker-enter-container-prod
```

### Just do the whole setup and creation in production

This command builds the base image, then the production image
and then creates a production container.

```bash
$ make docker-prod
```
