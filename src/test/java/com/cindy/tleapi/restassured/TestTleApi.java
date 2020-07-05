package com.cindy.tleapi.restassured;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test the TLE API using Rest-assured.
 */
public class TestTleApi {

    @Test
    public void checkConfig() {

        System.out.println("TestTleAPI::checkConfig:");
        assertThat(RestAssured.config(), instanceOf(RestAssuredConfig.class));
    }

    @Test
    public void getElsets() {
        System.out.println("TestTleAPI::getElsets:");
        RestAssured.
                given().
                    log().all().
                when().
                    get("http://localhost:8981/elsets").
                then().
                    log().body().
                    assertThat().
                    statusCode(200);
    }

    @Test
    public void getElsetInvalidSatno() {

        System.out.println("TestTleAPI::getElsetInvalidSatno:");
        RestAssured.
                given().
                    log().all().
                when().
                    get("http://localhost:8981/elsets/abcde").
                then().
                    assertThat().
                    statusCode(400);
    }

    @Test
    public void getElsetSatnoNotFound() {

        System.out.println("TestTleAPI::getElsetSatnoNotFound:");
        RestAssured.
                given().
                    log().all().
                when().
                    get("http://localhost:8981/elsets/1000").
                then().
                    assertThat().
                    statusCode(404);
    }

    @Test
    public void getElset() {

        System.out.println("TestTleAPI::getElset:");

        Response response = given().
                get("http://localhost:8981/elsets/44383");
        int statusCode = response.getStatusCode();
        assertThat(statusCode, is(200));

        System.out.println("TestTleAPI::getElset: response   = " + response);
        response.prettyPrint();
        System.out.println("TestTleAPI::getElset: statusCode = " + statusCode);
    }

    @Test
    public void measureResponseTimeInMilliSeconds() {

        System.out.println("TestTleAPI::measureResponseTimeInMilliSeconds:");

        long timeinMilliseconds =  RestAssured.
                given().
                when().
                    get("http://localhost:8981/elsets").
                    timeIn(TimeUnit.MILLISECONDS);

        System.out.println("Response time = " + timeinMilliseconds + " ms");}
}
