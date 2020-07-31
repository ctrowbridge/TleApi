package com.cindy.tleapi.restassured;

import static io.restassured.RestAssured.*;

import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Headers;
import io.restassured.response.*;

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

        System.out.println("Response time = " + timeinMilliseconds + " ms");
    }

    @Test
    public void post() {

        System.out.println("TestTleAPI::post:");

        Response response =
                when().
                        post("http://localhost:8981/elsets/" +
                                "?line1=TEMPSAT 1              "+
                                "&line2=1 00900U 64063C   20199.53174448  .00000207  00000-0  21239-3 0  9994"+
                                "&line3=2 00900  90.1522  28.8730 0026176 330.4877  57.3871 13.73393904774586").
                        then().
                        contentType("text/plain").
                        statusCode(200).
                        extract().
                        response();

        Headers headers = response.headers();
        System.out.println("TestTleAPI::post: headers = \"" + headers + "\"");
        response.prettyPrint();
    }

    @Test
    public void postWithBadLineNumber() {

        System.out.println("TestTleAPI::postWithBadLineNumber:");

        Response response =
                when().
                        post("http://localhost:8981/elsets/" +
                                "?line1=TEMPSAT 1              "+
                                "&line2=7 00900U 64063C   20199.53174448  .00000207  00000-0  21239-3 0  9994"+
                                "&line3=2 00900  90.1522  28.8730 0026176 330.4877  57.3871 13.73393904774586").
                        then().
                        contentType("text/plain").
                        statusCode(404).
                        extract().
                        response();

        Headers headers = response.headers();
        System.out.println("TestTleAPI::postWithBadLineNumber: headers = \"" + headers + "\"");
        response.prettyPrint();
    }

    @Test
    public void postWithMissingLine1() {

        System.out.println("TestTleAPI::postWithMissingLine1:");

        Response response =
                when().
                        post("http://localhost:8981/elsets/" +
                                "?line2=7 00900U 64063C   20199.53174448  .00000207  00000-0  21239-3 0  9994"+
                                "&line3=2 00900  90.1522  28.8730 0026176 330.4877  57.3871 13.73393904774586").
                        then().
                        contentType("text/plain").
                        statusCode(404).
                        extract().
                        response();

        Headers headers = response.headers();
        System.out.println("TestTleAPI::postWithMissingLine1: headers = \"" + headers + "\"");
        response.prettyPrint();
    }

    @Test
    public void postWithMissingLine2() {

        System.out.println("TestTleAPI::postWithMissingLine2:");

        Response response =
                when().
                        post("http://localhost:8981/elsets/" +
                                "?line1=TEMPSAT 1              " +
                                "&line3=2 00900  90.1522  28.8730 0026176 330.4877  57.3871 13.73393904774586").
                        then().
                        contentType("text/plain").
                        statusCode(404).
                        extract().
                        response();

        Headers headers = response.headers();
        System.out.println("TestTleAPI::postWithMissingLine2: headers = \"" + headers + "\"");
        response.prettyPrint();
    }

    @Test
    public void postWithMissingLine3() {

        System.out.println("TestTleAPI::postWithMissingLine3:");

        Response response =
                when().
                        post("http://localhost:8981/elsets/" +
                                "?line1=TEMPSAT 1              " +
                                "&line2=7 00900U 64063C   20199.53174448  .00000207  00000-0  21239-3 0  9994").
                        then().
                        contentType("text/plain").
                        statusCode(404).
                        extract().
                        response();

        Headers headers = response.headers();
        System.out.println("TestTleAPI::postWithMissingLine3: headers = \"" + headers + "\"");
        response.prettyPrint();
    }
}
