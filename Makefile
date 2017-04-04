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

# Create a base image
docker-baseimage:
	@echo Building a base image ...
	@docker build -t tobot/tobot-baseimage -f Dockerfile.baseimage .
	@echo Done! Image is named tobot/tobot-baseimage

# Create a production image
docker-image-prod:
	@echo Building a image for production ...
	@docker build -t tobot/tobot-prod -f Dockerfile.production .
	@echo Done! Image is named tobot/tobot-prod

# Create a development image
docker-image:
	@echo Building a image for development ...
	@docker build -t tobot/tobot .
	@echo Done! Image is named tobot/tobot

# Create a development container
docker-container:
	@if [ `docker ps -a | grep tobot-container-dev | wc -l` -ne "0" ]; then \
	echo "Found existing tobot-container-dev. Replacing it ..."; \
	make docker-container-removed; \
	fi
	@echo Creating a development container ...
	@docker run --name tobot-container-dev -d -p 8000:5032 -v ${v}:/tobot tobot/tobot
	@echo tobot-container created!

# Create a production container
docker-container-prod:
	@if [ `docker ps -a | grep tobot-container-prod$ | wc -l` -ne "0" ]; then \
	echo "Found existing tobot-container-prod. Replacing it ..."; \
	make docker-container-prod-removed; \
	fi
	@echo Creating a production container ...
	@docker run --name tobot-container-prod -d -p 8081:5032 tobot/tobot-prod
	@echo tobot-container-prod created!

# Remove development container
docker-container-removed:
	@echo Removing container...
	@docker stop tobot-container-dev
	@docker rm tobot-container-dev
	@echo tobot-container-dev removed!

# Remove production container
docker-container-prod-removed:
	@echo Removing production container...
	@docker stop tobot-container-prod
	@docker rm tobot-container-prod
	@echo tobot-container-prod removed!

# Enter the development container
docker-enter-container:
	@echo Entering tobot-container-dev ...
	@docker exec -it tobot-container-dev /bin/bash

# Enter the production container
docker-enter-container-prod:
	@echo Entering tobot-container-prod ...
	@docker exec -it tobot-container-prod /bin/bash

# Create the whole development container from scratch
docker: docker-baseimage docker-image docker-container

# Create the whole production container from scratch
docker-prod: docker-baseimage docker-image-prod docker-container-prod
