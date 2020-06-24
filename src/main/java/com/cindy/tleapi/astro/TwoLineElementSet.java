package com.cindy.tleapi.astro;

public class TwoLineElementSet extends ElementSet {

    private String name;
    private int satelliteNumber;
    private String classification;
    private String internationalDesignator;
    private int epochYear;
    private double epochDay;
    private double meanMotionDeriv1;
    private double meanMotionDeriv2;
    private double bstar;
    private int elementSetNum;

    // https://www.celestrak.com/NORAD/documentation/tle-fmt.php
    private final static String line00 = "123456789012345678901234567890123456789012345678901234567890123456780";
    private final static String line10 = "         1         2         3         4         5         6         ";
    private final static String line11 = "1 NNNNNU NNNNNAAA NNNNN.NNNNNNNN +.NNNNNNNN +NNNNN-N +NNNNN-N N NNNNN";
    private final static String line22 = "2 NNNNN NNN.NNNN NNN.NNNN NNNNNNN NNN.NNNN NNN.NNNN NN.NNNNNNNNNNNNNN";

    public TwoLineElementSet(String name,
                             int satelliteNumber,
                             String classification,
                             String internationalDesignator,
                             int epochYear,
                             double epochDay,
                             double meanMotionDeriv1,
                             double meanMotionDeriv2,
                             double bstar,
                             int elementSetNum) {
        this(satelliteNumber, classification, internationalDesignator, epochYear, epochDay,
                meanMotionDeriv1, meanMotionDeriv2, bstar, elementSetNum);
        this.name = name;
    }

    public TwoLineElementSet(int satelliteNumber,
                             String classification,
                             String internationalDesignator,
                             int epochYear,
                             double epochDay,
                             double meanMotionDeriv1,
                             double meanMotionDeriv2,
                             double bstar,
                             int elementSetNum) {
        this.name = "";
        this.satelliteNumber = satelliteNumber;
        this.classification = classification;
        this.internationalDesignator = internationalDesignator;
        this.epochYear = epochYear;
        this.epochDay = epochDay;
        this.meanMotionDeriv1 = meanMotionDeriv1;
        this.meanMotionDeriv2 = meanMotionDeriv2;
        this.bstar = bstar;
        this.elementSetNum = elementSetNum;
    }

    public TwoLineElementSet() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }
    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getSatelliteNumber() {
        return satelliteNumber;
    }
    public void setSatelliteNumber(int satelliteNumber) {
        this.satelliteNumber = satelliteNumber;
    }

    public java.lang.String getInternationalDesignator() {
        return internationalDesignator;
    }
    public void setInternationalDesignator(java.lang.String internationalDesignator) {
        this.internationalDesignator = internationalDesignator;
    }

    public int getEpochYear() {
        return epochYear;
    }
    public void setEpochYear(int epochYear) {
        this.epochYear = epochYear;
    }

    public double getEpochDay() {
        return epochDay;
    }

    public void setEpochDay(double epochDay) {
        this.epochDay = epochDay;
    }

    public double getMeanMotionDeriv1() {
        return meanMotionDeriv1;
    }

    public void setMeanMotionDeriv1(double meanMotionDeriv1) {
        this.meanMotionDeriv1 = meanMotionDeriv1;
    }

    public double getMeanMotionDeriv2() {
        return meanMotionDeriv2;
    }
    public void setMeanMotionDeriv2(double meanMotionDeriv2) {
        this.meanMotionDeriv2 = meanMotionDeriv2;
    }

    public double getBstar() {
        return bstar;
    }
    public void setBstar(double bstar) {
        this.bstar = bstar;
    }

    public int getElementSetNum() {
        return elementSetNum;
    }

    public void setElementSetNum(int elementSetNum) {
        this.elementSetNum = elementSetNum;
    }

    /**
     * Import from two line ASCII element set
     */
    public void importElset(String line1, String line2) {
        parseLine1(line1);
    }

    /**
     * Import from three line ASCII element set. Line 1 contains satellite name
     */
    public void importElset(String line1, String line2, String line3) {
        name = line1.substring(0, 24);
        System.out.println("importElset: name = " + name);
        parseLine1(line2);
        parseLine2(line3);
    }

    private void parseLine1(String line1) {

        System.out.println("*** parseLine1: line1 = \"" + line1 + "\"");

        String lineNumber = line1.substring(0, 1);
        System.out.println("*** parseLine1: lineNumber = \"" + lineNumber + "\"");
        int lineNumberInt = Integer.parseInt(lineNumber);

        String satNumber = line1.substring(2, 7);
        System.out.println("*** parseLine1: satNumber = \"" + satNumber + "\"");
        satNumber = satNumber.stripLeading();
        satelliteNumber = Integer.parseInt(satNumber);

        classification = line1.substring(7, 8);
        System.out.println("*** parseLine1: classification = \"" + classification + "\"");

        internationalDesignator = line1.substring(9, 17);
        System.out.println("*** parseLine1: internationalDesignator = \"" + internationalDesignator + "\"");

        String epochYearStr = line1.substring(18, 20);
        System.out.println("*** parseLine1: epochYearStr = \"" + epochYearStr + "\"");
        epochYear = Integer.parseInt(epochYearStr);

        String epochDayStr = line1.substring(20, 32);
        System.out.println("*** parseLine1: epochDayStr = \"" + epochDayStr + "\"");
        epochDay = Double.parseDouble(epochDayStr);

        String meanMotionDeriv1Str = line1.substring(33, 43);
        System.out.println("*** parseLine1: meanMotionDeriv1Str = \"" + meanMotionDeriv1Str + "\"");
        meanMotionDeriv1 = Double.parseDouble(meanMotionDeriv1Str);

        parseMeanMotionDeriv2(line1);
        parseBstar(line1);

        String elementSetNumStr = line1.substring(64, 68);
        System.out.println("*** parseLine1: elementSetNumStr = \"" + elementSetNumStr + "\"");
        elementSetNumStr = elementSetNumStr.stripLeading();
        System.out.println("*** parseLine1: elementSetNumStr = \"" + elementSetNumStr + "\"");
        elementSetNum = Integer.parseInt(elementSetNumStr);
    }

    private void parseMeanMotionDeriv2(String line1) {

        String meanMotionDeriv2Str = line1.substring(44, 50);
        System.out.println("*** parseLine1: meanMotionDeriv2Str = \"" + meanMotionDeriv2Str + "\"");
        String meanMotionDerivExpString = line1.substring(50, 52);
        System.out.println("*** parseLine1: meanMotionDerivExpString = \"" + meanMotionDerivExpString + "\"");
        int exp = Integer.parseInt(meanMotionDerivExpString);
        System.out.println("*** parseLine1: exp = " + exp);
        meanMotionDeriv2 = Double.parseDouble(meanMotionDeriv2Str);
        System.out.println("*** parseLine1: meanMotionDeriv2 = " + meanMotionDeriv2);
        meanMotionDeriv2 = meanMotionDeriv2*0.00001;
        System.out.println("*** parseLine1: meanMotionDeriv2 = " + meanMotionDeriv2);
        meanMotionDeriv2 = meanMotionDeriv2*Math.pow(10, exp);
        System.out.println("*** parseLine1: meanMotionDeriv2 = " + meanMotionDeriv2);
    }

    private void parseBstar(String line1) {

        String bstarStr = line1.substring(53, 59);
        String bstarExpStr = line1.substring(59, 61);
        int exp = Integer.parseInt(bstarExpStr);
        bstar = Double.parseDouble(bstarStr);
        bstar = bstar*0.00001;
        bstar = bstar*Math.pow(10, exp);
    }

    private void parseLine2(String line2) {
        System.out.println("*** parseLine2: line2 = \"" + line2 + "\"");
    }

    @Override
    public String toString() {
        return "TwoLineElementSet{" +
                "name='" + name + '\'' +
                ", satelliteNumber=" + satelliteNumber +
                ", classification='" + classification + '\'' +
                ", internationalDesignator='" + internationalDesignator + '\'' +
                ", epochYear=" + epochYear +
                ", epochDay=" + epochDay +
                ", meanMotionDeriv1=" + meanMotionDeriv1 +
                ", meanMotionDeriv2=" + meanMotionDeriv2 +
                ", bstar=" + bstar +
                ", elementSetNum=" + elementSetNum +
                '}';
    }
}
