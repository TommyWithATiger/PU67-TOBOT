# TOBOT

## Development environment

### Tools to get started

You will need:
 - [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
 - [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - [Node.js](https://nodejs.org/en/)

### SetUp for dev environment

#### Backend

```bash
$ cd java # Go into the server section.
$ mvn clean install # Compile code.
$ mvn exec:java -D exec.mainClass=main.ServerInitializer # Run server.
```

#### Frontend

```bash
$ cd web # Go into the web section.
$ npm install # Install frontend dependecies.
$ npm run dev # This will automatically run a dev server with hot-reload.
```

## Production environment

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
