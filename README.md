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
* To create and load the database, run the *LoadDB* class
in the  **com.cindy.tleapi.db** package:

    `> mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="com.cindy.tleapi.db.LoadDB"`
    
This defaults to the "2019-006.txt" file in the data directory. If you want to import another element file:

    `> mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="com.cindy.tleapi.db.LoadDB" -Dexec.args=<file name>"`
    
For example:

    `> mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="com.cindy.tleapi.db.LoadDB" -Dexec.args="data\goes.txt"
    
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

    `> mvn exec:java -Dexec.mainClass="com.cindy.tleapi.api.ApiMain"`
* To run the REST-Assured tests,
run the *TestTleApi* class in the **com.cindy.tleapi.restassured** package
after starting the API. 

    `> mvn -Dtest="com.cindy.tleapi.restassured.TestTleApi" test`

# API Documentation

The TLE API is a REST API. It currently outputs two line element set parameters in JSON 
format.  For details on the two line element set format, see 
https://en.wikipedia.org/wiki/Two-line_element_set

## List all Element Sets
http://localhost:8981/elsets

Retrieves a list of all elsets in the TLE database.

http://localHost:8981/elsets?page=2&pageSize=20

Retrieves a list of all elsets in the TLE database on the given page, with the given page size.
'page' starts at 1 and must be > 1, with default = 1.
'pageSize' must be > 1, with default = 20.

## List one Element Set
http://localhost:8981/elsets/<num>

Retrieves one elset given a number.  For example:

http://localhost:8981/elsets/44134

returns
```
{
    "name": "MICROSAT-R DEB          ",
    "satelliteNumber": 44134,
    "classification": "U",
    "internationalDesignator": "19006V  ",
    "epochYear": 20,
    "epochDay": 174.41671131,
    "meanMotionDeriv1": 1.6095E-4,
    "meanMotionDeriv2": 1.4248000000000003E-6,
    "bstar": 1.9296000000000002E-4,
    "elementSetNum": 999,
    "inclination": 96.0467,
    "rightAscension": 333.7157,
    "eccentricity": 0.0479877,
    "argumentOfPerigee": 321.356,
    "meanAnomaly": 35.4204,
    "meanMotion": 14.85662621,
    "revolutionNum": 6578
}
```

