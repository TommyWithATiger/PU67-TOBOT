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


# Building backend
backend-build:
	@echo Building java application ...
	@cd java && mvn clean install
	@echo Done!


# Running backend
backend-run:
	@echo Running server on http://localhost:5032
	@cd java && mvn exec:java -D exec.mainClass=main.ServerInitializer
	@echo Process killed.


# Make the backend
backend: backend-build backend-run
