# TOBOT

[![Build Status](https://ci.niklasmh.no/api/badges/TommyWithATiger/TOBOT/status.svg)](https://ci.niklasmh.no/TommyWithATiger/TOBOT)

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

If you are experienced with docker alone, you can build the docker images as you want to fit your own enivronment.
Else, we reccommend using docker-compose to run the project.

Simply run:

```bash
$ docker-compose up # Automatically build and run. Add -d to run in own thread.
$ docker-compose down # If you are running in an own thread, this command will shut down the containers correctly.
```
