package com.cindy.tleapi.test;

import com.cindy.tleapi.astro.Angle;
import com.cindy.tleapi.astro.TwoLineElementSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the TwoLineElementSet class
 */
public class TestTwoLineElementSet {

    private static final Logger logger = LogManager.getLogger();

    // Test element set (two lines)
    private final int satnumExpected = 23455;
    private final String classExpected = "U";
    private final String intlDesigExpected = "94089A  ";
    private final int epochYearExpected = 97;
    private final double epochDayExpected = 320.90946019;
    private final double meanMotionDeriv1Expected = 0.00000140;
    private final double meanMotionDeriv2Expected = 0.0;
    private final double bstarExpected = .00010191;
    private final int elsetNumExpected = 262;
    private final double incDegExpected = 99.0090;
    private final double raDegExpected = 272.6745;
    private final double eccExpected = 0.0008546;
    private final double argPerigeeDegExpected = 223.1686;
    private final double meanAnomalyDegExpected = 136.8816;
    private final double meanMotionExpected = 14.11711747;
    private final int revNoExpected = 14849;

    private final static String lineee = "123456789012345678901234567890123456789012345678901234567890123456789";
    private final static String linexx = "         1         2         3         4         5         6         ";
    private final static String line21 = "1 23455U 94089A   97320.90946019  .00000140  00000-0  10191-3 0  2621";
    private final static String line22 = "2 23455  99.0090 272.6745 0008546 223.1686 136.8816 14.11711747148495";

    // Test element set three lines and mean motion derive 2
    private final String nameExpected_3 = "MICROSAT-R DEB          ";
    private final double meanMotionDeriv1Expected_3 = 0.00016095;
    private final double meanMotionDeriv2Expected_3 = 0.14248e-5;
    private final double bstarExpected_3 = 0.19296e-3;
    private final int elsetNumExpected_3 = 999;
    private final int revNumExpected_3 = 6578;

    private final static String line30 = "MICROSAT-R DEB                                                       ";
    private final static String line31 = "1 44134U 19006V   20174.41671131  .00016095  14248-5  19296-3 0  9995";
    private final static String line32 = "2 44134  96.0467 333.7157 0479877 321.3560  35.4204 14.85662621 65786";

    // Test element set with negative bstar
    private final static double bstarExpected_4 = -0.11606e-4;

    private final static String line40 = "ISS (ZARYA)                                                          ";
    private final static String line41 = "1 25544U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927";
    private final static String line42 = "2 25544  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537";

    // Test element set against OMM
    private final static String line50 = "GOES 9 [P]";
    private final static String line51 = "1 23581U 95025A   07064.44075725 -.00000113  00000-0  10000-3 0  9250";
    private final static String line52 = "2 23581   3.0539  81.7939 0005013 249.2363 150.1602  1.00273272 43169";

    /**
     * CREATION_DATE = 2007-065T16:00:00
     * ORIGINATOR = NOAA/USA
     * OBJECT_NAME = GOES 9
     * OBJECT_ID = 1995-025A
     * CENTER_NAME = EARTH
     * REF_FRAME = TEME
     * TIME_SYSTEM = UTC
     * MEAN_ELEMENT_THEORY = SGP/SGP4
     */
    private final String nameExpected_5 = "GOES 9 [P]            ";
    private final String intlDesExpected_5x = "1995-025A";
    private final String intlDesExpected_5 = "95025A  ";

    /**
     * EPOCH = 2007-064T10:34:41.4264
     * MEAN_MOTION = 1.00273272
     * ECCENTRICITY = 0.0005013
     * INCLINATION = 3.0539
     * RA_OF_ASC_NODE = 81.7939
     * ARG_OF_PERICENTER = 249.2363
     * MEAN_ANOMALY = 150.1602
     * GM = 398600.8
     * EPHEMERIS_TYPE = 0
     * CLASSIFICATION_TYPE = U
     * NORAD_CAT_ID = 23581
     * ELEMENT_SET_NO = 0925
     * REV_AT_EPOCH = 4316
     */
    private final int revNumExpected_5 = 4316;
    /**
     * BSTAR = 0.0001
     * MEAN_MOTION_DOT = -0.00000113
     * MEAN_MOTION_DDOT = 0.0
     */

    @Test
    public void testConstructor2() {

        TwoLineElementSet elset =  new TwoLineElementSet(satnumExpected, classExpected,
                intlDesigExpected, epochYearExpected,
                epochDayExpected,
                meanMotionDeriv1Expected,
                meanMotionDeriv2Expected,
                bstarExpected, elsetNumExpected,
                new Angle(incDegExpected, Angle.AngleUnits.DEGREES),
                new Angle(raDegExpected, Angle.AngleUnits.DEGREES),
                eccExpected,
                new Angle(argPerigeeDegExpected, Angle.AngleUnits.DEGREES),
                new Angle(meanAnomalyDegExpected, Angle.AngleUnits.DEGREES),
                meanMotionExpected, revNoExpected);

        Assert.assertEquals(elset.getSatelliteNumber(), satnumExpected);
        Assert.assertEquals(elset.getClassification(), classExpected);
        Assert.assertEquals(elset.getInternationalDesignator(), intlDesigExpected);
        Assert.assertEquals(elset.getEpochYear(), epochYearExpected);
        Assert.assertEquals(elset.getEpochDay(), epochDayExpected);
        Assert.assertEquals(elset.getMeanMotionDeriv1(), meanMotionDeriv1Expected);
        Assert.assertEquals(elset.getMeanMotionDeriv2(), meanMotionDeriv2Expected);
        Assert.assertEquals(elset.getBstar(), bstarExpected);
        Assert.assertEquals(elset.getElementSetNum(), elsetNumExpected);
        Assert.assertEquals(elset.getInclination().getDegrees(), incDegExpected);
        Assert.assertEquals(elset.getRightAscension().getDegrees(), raDegExpected);
        Assert.assertEquals(elset.getEccentricity(), eccExpected);
        Assert.assertEquals(elset.getArgumentOfPerigee().getDegrees(), argPerigeeDegExpected);
        Assert.assertEquals(elset.getMeanAnomaly().getDegrees(), meanAnomalyDegExpected);
        Assert.assertEquals(elset.getMeanMotion(), meanMotionExpected);
        Assert.assertEquals(elset.getRevolutionNum(), revNoExpected);

        logger.info("elset = " + elset);
    }

    @Test
    public void testImport2() {

        TwoLineElementSet elset = new TwoLineElementSet();

        elset.importElset(line21, line22);

        Assert.assertEquals(elset.getSatelliteNumber(), satnumExpected);
        Assert.assertEquals(elset.getClassification(), classExpected);
        Assert.assertEquals(elset.getInternationalDesignator(), intlDesigExpected);
        Assert.assertEquals(elset.getEpochYear(), epochYearExpected);
        Assert.assertEquals(elset.getEpochDay(), epochDayExpected, 0.00000001);
        Assert.assertEquals(elset.getMeanMotionDeriv1(), meanMotionDeriv1Expected, 0.00000001);
        Assert.assertEquals(elset.getBstar(), bstarExpected, 0.00000001);
        Assert.assertEquals(elset.getElementSetNum(), elsetNumExpected);
        Assert.assertEquals(elset.getInclination().getDegrees(), incDegExpected, 0.00000001);
        Assert.assertEquals(elset.getRightAscension().getDegrees(), raDegExpected, 0.00000001);
        Assert.assertEquals(elset.getEccentricity(), eccExpected);
        Assert.assertEquals(elset.getArgumentOfPerigee().getDegrees(), argPerigeeDegExpected);
        Assert.assertEquals(elset.getMeanAnomaly().getDegrees(), meanAnomalyDegExpected);
        Assert.assertEquals(elset.getMeanMotion(), meanMotionExpected);
        Assert.assertEquals(elset.getRevolutionNum(), revNoExpected);

        logger.info("elset = " + elset);
    }

    @Test
    public void testImport3() {

        TwoLineElementSet elset = new TwoLineElementSet();

        elset.importElset(line30, line31, line32);
        logger.info("elset = " + elset);

        Assert.assertEquals(elset.getName(), nameExpected_3);
        Assert.assertEquals(elset.getMeanMotionDeriv1(), meanMotionDeriv1Expected_3, 0.00000001);
        Assert.assertEquals(elset.getMeanMotionDeriv2(), meanMotionDeriv2Expected_3, 0.00000001);
        Assert.assertEquals(elset.getBstar(), bstarExpected_3, 0.00000001);
        Assert.assertEquals(elset.getElementSetNum(), elsetNumExpected_3);
        Assert.assertEquals(elset.getRevolutionNum(), revNumExpected_3);
    }

    @Test
    public void testImportNegativeBstar() {

        TwoLineElementSet elset = new TwoLineElementSet();

        elset.importElset(line40, line41, line42);
        logger.info("elset = " + elset);

        Assert.assertEquals(elset.getBstar(), bstarExpected_4, 0.00000001);
    }

    @Test
    public void testImportOMM() {

        TwoLineElementSet elset = new TwoLineElementSet();

        elset.importElset(line50, line51, line52);
        logger.info("elset = " + elset);

        Assert.assertEquals(elset.getName().trim(), nameExpected_5.trim());
        Assert.assertEquals(elset.getRevolutionNum(), revNumExpected_5);
        Assert.assertEquals(elset.getInternationalDesignator(), intlDesExpected_5);
    }

}
