package com.cindy.tleapi.restassured;

//import com.cindy.tleapi.astro.TwoLineElementSet;


import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;

import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.*;
import io.restassured.response.ResponseBody;

import org.jetbrains.annotations.TestOnly;
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
        assertThat(config(), instanceOf(RestAssuredConfig.class));
    }

    @Test
    public void getElsetsReturnsStatusCode200() {

        System.out.println("TestTleAPI::getElsetsReturnsStatusCode200:");
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
        given().
            log().all().
        when().
            get("http://localhost:8981/elsets/1000").
        then().
            assertThat().
            statusCode(404);
    }

    @Test
    public void getElsets() {

        System.out.println("TestTleAPI::getElsets:");
        Response response =
            when().
                get("http://localhost:8981/elsets").
            then().
                contentType("application/json").
                statusCode(200).

            extract().
                response();

        Headers headers = response.headers();
        System.out.println("TestTleAPI::getElsets: headers = \"" + headers + "\"");
        response.prettyPrint();
    }

    @Test
    public void getElset() {

        // MICROSAT-R DEB
        // 1 44383U 19006DE  20174.36429503  .00026776  74079-6  45268-3 0  9993
        // 2 44383  96.1850 305.6869 0819710 119.1222 249.4533 14.05878177 56249

        System.out.println("TestTleAPI::getElset:");

        Response response =
                when().
                    get("http://localhost:8981/elsets/44383").
                then().
                    contentType("application/json").
                    statusCode(200).
                    body("satelliteNumber", equalTo(44383)).
                    body("name", equalTo("MICROSAT-R DEB          ")).
                    body("classification", equalTo("U")).
                    body("internationalDesignator", equalTo("19006DE ")).
                    body("epochYear", equalTo(20)).
                    body("epochDay", equalTo(174.36429503f)).
                    body("meanMotionDeriv1", equalTo(.00026776f)).
                    body("meanMotionDeriv2", equalTo(.74079e-6f)).
                    body("bstar", equalTo(0.45268e-3f)).
                    body("elementSetNum", equalTo(999)).
                    body("inclination", equalTo(96.1850f)).
                    body("rightAscension", equalTo(305.6869f)).
                    body("eccentricity", equalTo(.0819710f)).
                    body("argumentOfPerigee", equalTo(119.1222f)).
                    body("meanAnomaly", equalTo(249.4533f)).
                    body("meanMotion", equalTo(14.05878177f)).
                    body("revolutionNum", equalTo(5624)).
                extract().
                    response();

        System.out.println("TestTleAPI::getElset: response   = " + response);
        response.prettyPrint();
        System.out.println("TestTleAPI::getElset: statusLine = \"" +
                response.statusLine() + "\"");

        String contentType = response.contentType();
        System.out.println("TestTleAPI::getElset: contentType = \"" + contentType + "\"");

        Headers headers = response.headers();
        System.out.println("TestTleAPI::getElset: headers = \"" + headers + "\"");
    }

    @Test
    public void measureResponseTimeInMilliSeconds() {

        System.out.println("TestTleAPI::measureResponseTimeInMilliSeconds:");

        long timeinMilliseconds =
            given().
            when().
                get("http://localhost:8981/elsets").
                timeIn(TimeUnit.MILLISECONDS);

        System.out.println("Response time = " + timeinMilliseconds + " ms");}
}
