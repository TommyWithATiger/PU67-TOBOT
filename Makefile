# This file contains commands for those who has make installed.

.PHONY: all

# Default `make` will build frontend prod and start backend
all: frontend-build-prod backend


# Building frontend
frontend-build:
	@echo Changing directory ...
	@cd web
	@echo Installing node dependencies ...
	@npm install --silent
	@echo Done!


# Building frontend for production
frontend-build-prod:
	@echo Changing directory ...
	@cd web
	@echo Installing node dependencies ...
	@npm install --silent
	@echo Compiling frontend ...
	@npm run build
	@echo Done!


# Running frontend
frontend-run-dev:
	@echo Changing directory ...
	@cd web
	@echo Running dev environment on port http://localhost:8080 ...
	@npm run dev
	@echo Process killed.


# Building backend
backend-build:
	@echo Building java application ...
	@mvn clean install
	@echo Done!


# Running backend
backend-run:
	@echo Running server on http://localhost:5032
	@mvn exec:java -D exec.mainClass=main.ServerInitializer
	@echo Process killed.


# Make the backend
backend: backend-build backend-run
