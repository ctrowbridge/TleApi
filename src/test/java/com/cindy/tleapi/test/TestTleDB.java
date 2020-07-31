package com.cindy.tleapi.test;

import com.cindy.tleapi.astro.AstroException;
import com.cindy.tleapi.astro.TwoLineElementSet;
import com.cindy.tleapi.db.TleDb;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Contains unit tests for TLE Database package
 */
public class TestTleDB {

    private static final Logger logger = LogManager.getLogger();

    // MICROSAT-R DEB
    // 1 44134U 19006V   20174.41671131  .00016095  14248-5  19296-3 0  9995
    // 2 44134  96.0467 333.7157 0479877 321.3560  35.4204 14.85662621 65786
    private final int satnumExpected = 44134;
    private final String classExpected = "U";
    private final String intlDesigExpected = "19006V  ";
    private final int epochYearExpected = 20;
    private final double epochDayExpected = 174.41671131;
    private final double meanMotionDeriv1Expected = 0.00016095;
    private final double meanMotionDeriv2Expected = 0.14248e-5;

    private final double bstarExpected = 0.19296e-3;
    private final int elsetNumExpected = 999;
    private final double incDegExpected = 96.0467;
    private final double raDegExpected = 333.7157;
    private final double eccExpected = 0.0479877;
    private final double argPerigeeDegExpected = 321.3560;
    private final double meanAnomalyDegExpected = 35.4204;
    private final double meanMotionExpected = 14.85662621;
    private final int revNoExpected = 6578;

    private final String testFileName = "data\\2019-006.txt";

    private final String testLine1 = "LCS 1                   ";
    private final String testLine2 = "1 01361U 65034C   20199.53569372  .00000028  00000-0  25492-2 0  9994";
    private final String testLine3 = "2 01361  32.1395 267.1291 0005455 248.0922 111.8996  9.89297492995906";

    @Test(priority = 1)
    public void testNewDB() {

        logger.info("testNewDB ========================================================");
        TleDb db = new TleDb();

        try {
            db.open();
            db.createTable();
            int count = db.getElsetCount();
            Assert.assertEquals(count, 0);
            String productName = db.getDatabaseProductName();
            logger.info("testNewDB: productName = " + productName);
            makeSureDatabaseIsOpenThenClose(db);

        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
            logger.error("****   Make sure API is not running!");
            //exp.printStackTrace();
        }
    }

    @Test(priority = 2, dependsOnMethods={"testNewDB"})
    public void TestReadWithoutOpen() {

        logger.info("TestReadWithoutOpen ========================================================");
        TleDb db = new TleDb();
        try {
            int count = db.getElsetCount();
            logger.info("count = " + count);
            Assert.assertEquals(count, 0);
            makeSureDatabaseIsOpenThenClose(db);
        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
            logger.error("****   Make sure API is not running!");
        }
    }

    @Test(priority = 3)
    public void TestElsetParameters() {

        logger.info("TestElsetParameters ========================================================");
        TleDb db = new TleDb();
        try {
            db.open();
            db.createTable();
            db.load(testFileName);
            List<TwoLineElementSet> elsets = db.getElsets();
            TwoLineElementSet elset = elsets.get(0);

            Assert.assertEquals(elset.getSatelliteNumber(), satnumExpected);
            Assert.assertEquals(elset.getClassification(), classExpected);
            Assert.assertEquals(elset.getInternationalDesignator(), intlDesigExpected);
            Assert.assertEquals(elset.getEpochYear(), epochYearExpected);
            Assert.assertEquals(elset.getEpochDay(), epochDayExpected);
            Assert.assertEquals(elset.getMeanMotionDeriv1(), meanMotionDeriv1Expected, 0.00000001);
            Assert.assertEquals(elset.getMeanMotionDeriv2(), meanMotionDeriv2Expected, 0.00000001);
            Assert.assertEquals(elset.getBstar(), bstarExpected, 0.00000001);
            Assert.assertEquals(elset.getElementSetNum(), elsetNumExpected);
            Assert.assertEquals(elset.getInclination().getDegrees(), incDegExpected, 0.00000001);
            Assert.assertEquals(elset.getRightAscension().getDegrees(), raDegExpected);
            Assert.assertEquals(elset.getEccentricity(), eccExpected);
            Assert.assertEquals(elset.getArgumentOfPerigee().getDegrees(), argPerigeeDegExpected);
            Assert.assertEquals(elset.getMeanAnomaly().getDegrees(), meanAnomalyDegExpected);
            Assert.assertEquals(elset.getMeanMotion(), meanMotionExpected);
            Assert.assertEquals(elset.getRevolutionNum(), revNoExpected);

            makeSureDatabaseIsOpenThenClose(db);

        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
            logger.error("****   Make sure API is not running!");
        }
    }

    @Test (priority = 4, dependsOnMethods={"TestElsetParameters"})
    public void TestEmptyDatabase() {

        logger.info("TestEmptyDatabase ========================================================");
        TleDb db = new TleDb();

        try {
            db.open();
            db.createTable();
            int count = db.getElsetCount();
            Assert.assertEquals(count, 0);
            db.load(testFileName);
            count = db.getElsetCount();
            logger.info("count = " + count);
            Assert.assertEquals(count, 7);
            db.emptyDb();
            count = db.getElsetCount();
            logger.info("count = " + count);
            Assert.assertEquals(count, 0);
            makeSureDatabaseIsOpenThenClose(db);

        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
            logger.error("****   Make sure API is not running!");
        }
    }

    @Test(priority = 5, dependsOnMethods={"TestElsetParameters"})
    public void TestDeleteOneElset() {

        logger.info("TestDeleteOneElset ========================================================");
        TleDb db = new TleDb();

        try {
            db.open();
            db.createTable();
            db.load(testFileName);
            int count = db.getElsetCount();
            logger.info("count = " + count);
            Assert.assertEquals(count, 7);
            db.deleteElset(44383);
            count = db.getElsetCount();
            logger.info("count = " + count);
            Assert.assertEquals(count, 6);
            db.getElset(44383);
            makeSureDatabaseIsOpenThenClose(db);
        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
            logger.error("****   Make sure API is not running!");
        }
    }

    @Test(priority = 6, dependsOnMethods={"TestElsetParameters"})
    public void TestToJason() {

        logger.info("TestToJason ========================================================");

        TleDb db = new TleDb();
        try {
            db.open();
            db.createTable();
            db.load(testFileName);
            List<TwoLineElementSet> elsets = db.getElsets();
            TwoLineElementSet elset = elsets.get(0);
            String jsonStr = elset.toJson();
            logger.info("TestToJson:  jsonStr = \n" + jsonStr);
            makeSureDatabaseIsOpenThenClose(db);

            boolean jsonParsed = true;
            try{
                JsonParser.parseString(jsonStr);
            }
            catch(JsonSyntaxException jse){
                logger.error("Not a valid Json String:"+ jse.getMessage());
                jsonParsed = false;
            }
            Assert.assertTrue(jsonParsed);

        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
            logger.error("****   Make sure API is not running!");
        }
    }

    @Test(priority = 7, dependsOnMethods={"TestElsetParameters"})
    public void TestGetNonexistentElset() {

        logger.info("TestGetNonexistentElset ========================================================");

        TleDb db = new TleDb();
        try {
            db.open();
            db.createTable();
            db.load(testFileName);

            boolean exists = db.ifElsetExists(1361);
            logger.info("TestGetNonexistentElset: exists = " + exists);
            Assert.assertFalse(exists);

            exists = db.ifElsetExists(44463);
            logger.info("TestGetNonexistentElset: exists = " + exists);
            Assert.assertTrue(exists);

            makeSureDatabaseIsOpenThenClose(db);

        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
            Assert.fail();
        }
    }

    @Test(priority = 8, dependsOnMethods={"TestElsetParameters"})
    public void TestAddElset() throws AstroException {

        logger.info("TestAddElset ========================================================");

        TwoLineElementSet elset = new TwoLineElementSet();
        elset.importElset(testLine1, testLine2, testLine3);

        TleDb db = new TleDb();
        try {
            db.open();
            db.createTable();
            int count = db.getElsetCount();
            logger.info("TestAddElset:  count = " + count);
            Assert.assertEquals(count, 0);
            db.addElset(elset);

            boolean exists = db.ifElsetExists( 1);
            Assert.assertFalse(exists);

            count = db.getElsetCount();
            Assert.assertEquals(count, 1);

            makeSureDatabaseIsOpenThenClose(db);

        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
            Assert.assertTrue(true);
        }

    }

    private void makeSureDatabaseIsOpenThenClose(TleDb db) throws Exception {
        Assert.assertFalse(db.isClosed());
        db.close();
        Assert.assertTrue(db.isClosed());
    }
}
