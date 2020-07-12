# TleApi
Simple Two Line Element (TLE) API created to try out different unit and API testing techniques.

# Components

This repository consists of the following components:
## Java
* Package **com.cindy.tleapi.astro** - Contains simple Two Line Element Set parser.
* Package **com.cindy.tleapi.db** - Contains simple Two Line Element Set database
implemented in Derby.
* Package **com.cindy.tleapi.api** - Contains simple Two Line Element Set API implemented using
Javalin.
## Java Tests
* Package **com.cindy.tleapi.test** - Contains unit tests for the **com.cindy.tleapi.astro** (*TestTleDB*) and
**com.cindy.tleapi.db** (*TestTwoLineElementSet*) packages implemented usint TestNG.
* Package **com.cindy.tleapi.restassured** - Contains REST-Assured tests for the API.

## Postman
* TleApi Collection - Contains Postman queries used to test the API.

## Python
TBD

# Usage

* To update Maven dependencies and compile:

    `> mvn compile`
* To create and load the database,   run the *LoadDB* class
in the  **com.cindy.tleapi.db** package.

    `> mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="com.cindy.tleapi.db.LoadDB"`
    
* To run unit tests on the database, run the *TestTleDB* class
in the **com.cindy.tleapi.test** package.
    `> mvn -Dtest=TestTleDB test`
* To run unit test on the TwoLineElementSet class, run the *TestTwoLineElementSet* class
in the *com.cindy.tleapi.test** package.

    `> mvn -Dtest=TestTwoLineElementSet test`
    
* To start the API,                  run the *ApiMain* class
in the **com.cindy.tleapi.api** package. 
The API will run on
URL:  http://localhost:8981/
* To run the REST-Assured tests,
run the *TestTleApi* class in the **com.cindy.tleapi.restassured** package
after starting the API. 
