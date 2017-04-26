# This file contains commands for those who has make installed.

.PHONY: all

# Default `make` will build frontend prod and start backend
all: frontend-build-prod backend


# Building frontend
frontend-build:
	@echo Installing node dependencies ...
	@cd web && npm install --silent
	@echo Done!

# Building frontend for production
frontend-build-prod:
	@echo Installing node dependencies ...
	@cd web && npm install --silent
	@echo Compiling frontend ...
	@cd web && npm run build
	@echo Done!

# Running frontend
frontend-run-dev:
	@echo Changing directory and running dev environment on port http://localhost:8080 ...
	@cd web && npm run dev
	@echo Process killed.

# Build and run frontend in dev
frontend: frontend-build frontend-run-dev

# Building frontend
frontend-api:
	@echo Compiling frontend ...
	@cd web && npm run build
	@echo Done!

# Building backend
backend-build:
	@echo Building java application ...
	@cd java && mvn -Dmaven.test.skip=true -B install
	@echo Done!

# Building backend
backend-build-clean:
	@echo Building java application  from scratch ...
	@cd java && mvn -Dmaven.test.skip=true -B clean install
	@echo Done!

# Building backend with tests
backend-build-tests:
	@echo Builing java application with tests ...
	@cd java && mvn -B install -q
	@echo Done!

# Building backend with tests
backend-build-tests-clean:
	@echo Builing java application with tests from scratch ...
	@cd java && mvn -B clean install -q
	@echo Done!

# Running backend
backend-run:
	@echo Running server on http://localhost:5032
	@cd java && mvn exec:java -D exec.mainClass=main.ServerInitializer
	@echo Process killed.

# Make the backend
backend: backend-build backend-run

# Make the backend with tests
backend-tests: backend-build-tests backend-run

# Create the project in docker containers
docker:
	@echo Building the whole project ...
	@docker-compose up
