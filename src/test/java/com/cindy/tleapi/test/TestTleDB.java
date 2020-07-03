package com.cindy.tleapi.test;

import com.cindy.tleapi.astro.TwoLineElementSet;
import com.cindy.tleapi.db.TleDb;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

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

    @Test
    public void testNewDB() {

        logger.info("testNewDB ...");
        TleDb db = new TleDb();

        try {
            db.open();
            db.createTable();
            int count = db.getElsetCount();
            Assert.assertEquals(count, 0);
            db.close();
        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
        }
    }

    @Test
    public void TestReadWithoutOpen() {

        logger.info("TestReadWithoutOpen ...");
        TleDb db = new TleDb();
        try {
            int count = db.getElsetCount();
            logger.info("count = " + count);
            Assert.assertEquals(count, 0);
            db.close();
        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
        }
    }

    @Test
    public void TestElsetParameters() {

        logger.info("TestElsetParameters ...");
        TleDb db = new TleDb();
        try {
            db.open();
            db.createTable();
            db.load("data\\2019-006.txt");
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

            db.close();

        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
        }
    }

    @Test
    public void TestToJason() {
        TleDb db = new TleDb();
        try {
            db.open();
            db.createTable();
            db.load("data\\2019-006.txt");
            List<TwoLineElementSet> elsets = db.getElsets();
            TwoLineElementSet elset = elsets.get(0);
            String jsonStr = elset.toJson();
            logger.info("TestToJson:  jsonStr = \n" + jsonStr);

            boolean jsonParsed = true;
            try{
                JsonParser.parseString(jsonStr);
            }
            catch(JsonSyntaxException jse){
                System.out.println("Not a valid Json String:"+ jse.getMessage());
                jsonParsed = false;
            }
            Assert.assertTrue(jsonParsed);

        } catch (Exception exp) {
            logger.error("**** Error: + " + exp);
        }
    }
}
