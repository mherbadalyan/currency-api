# Currency Exchange REST API

This is a Spring Boot application that provides REST API endpoints for managing currencies and getting exchange rates.
Exchange rates are fetched from https://currencybeacon.com/, and they are scheduled to be updated every hour.

## Features

* Get a list of available currencies used in the project.
* Get exchange rates for a currency.
* Add a new currency for getting exchange rates.

## Requirements

* Docker

## Usage
Once the application is up and running, you can use the following endpoints to interact with the API:

* GET /currencies: Retrieve a list of currencies used in the project.
* GET /currencies/{currency}/exchange-rates: Get exchange rates for a specific currency.
* POST /currencies: Add a new currency for getting exchange rates.
  
## Database Schema
  The database schema is managed using Liquibase. You can find the schema 
  definition in the resources/db/changelog/changelog-master.yaml file.

## API Documentation
API documentation is available using OpenAPI Specification. You can access it at /swagger-ui.html endpoint when the application is running.

## Starting the Application
To start the application with Docker Compose, follow these steps:

1. Open a terminal or command prompt and navigate to the root directory of the project where the `docker-compose.yml` file is located.

2. Run the following command to start the Docker containers defined in the `docker-compose.yml` file:


    docker-compose up
