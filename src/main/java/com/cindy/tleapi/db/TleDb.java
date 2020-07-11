package com.cindy.tleapi.db;

import com.cindy.tleapi.astro.Angle;
import com.cindy.tleapi.astro.TwoLineElementSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Opens, creates and loads the TLE database.
 * TODO:  Add update
 */
public class TleDb {

    static final Logger logger = LogManager.getLogger();

    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String databaseURL = "jdbc:derby:scripts/tledb";

    private Connection conn = null;

    /**
     * Connects to the database.
     *
     * @throws Exception
     */
    public void open() throws Exception {

        logger.info("Open database ...");
        conn = DriverManager.getConnection(databaseURL);
        logger.info("  conn = " + conn);
    }

    /**
     * Closes the database.
     *
     * @throws Exception
     */
    public void close() throws Exception {
        logger.info("Close database ...");
        if (conn != null) {
            conn.close();
        }
    }

    public void executeSql(String sql) throws Exception {

        Statement statement = conn.createStatement();
        logger.info("Execute statement= " + statement);
        statement.execute(sql);
    }

    /**
     * Creates the TLEDB table.
     *
     * @throws Exception
     */
    public void createTable() throws Exception {

        logger.info("Create database table ...");
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();

        String sqlDrop = "drop table tledb";
        statement.execute(sqlDrop);

        // TODO:  Make variables with fields names
        String sql = "CREATE TABLE tledb";
        sql += "(";
        sql += "satelliteNumber int, ";
        sql += "name varchar(30),";
        sql += "classification varchar(1),";
        sql += "internationalDesignator varchar(10),";
        sql += "epochYear int,";
        sql += "epochDay double,";
        sql += "meanMotionDeriv1 double,";
        sql += "meanMotionDeriv2 double,";
        sql += "bstar double,";
        sql += "elementSetNum int,";
        sql += "inclination double,";
        sql += "rightAscension double,";
        sql += "eccentricity double,";
        sql += "argumentOfPerigee double,";
        sql += "meanAnomaly double,";
        sql += "meanMotion double,";
        sql += "revolutionNum int";
        sql += ")";

        logger.info(" sql = \"" + sql + "\"");
        statement.execute(sql);
    }

    /**
     * Loads the database from the input 3 line element set.
     * Existing data is not deleted.
     *
     * @param filename Input three line element set
     * @throws Exception
     */
    public void load(String filename) throws Exception {

        int count = 0;
        logger.info("load " + filename);
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();

        try {
            File inFile = new File(filename);
            Scanner sc = new Scanner(inFile);

            while (sc.hasNextLine()) {
                String line0 = sc.nextLine();
                String line1 = sc.nextLine();
                String line2 = sc.nextLine();
                count++;
                TwoLineElementSet elset = new TwoLineElementSet();
                elset.importElset(line0, line1, line2);

                String sql = "insert into tledb values ";

                sql += "(";
                sql += elset.getSatelliteNumber() + ", ";
                sql += "'" + elset.getName() + "', ";
                sql += "'" + elset.getClassification() + "', ";
                sql += "'" + elset.getInternationalDesignator() + "', ";
                sql += elset.getEpochYear() + ", ";
                sql += elset.getEpochDay() + ", ";
                sql += elset.getMeanMotionDeriv1() + ", ";
                sql += elset.getMeanMotionDeriv2() + ", ";
                sql += elset.getBstar() + ", ";
                sql += elset.getElementSetNum() + ", ";
                sql += elset.getInclination().getDegrees() + ", ";
                sql += elset.getRightAscension().getDegrees() + ", ";
                sql += elset.getEccentricity() + ", ";
                sql += elset.getArgumentOfPerigee().getDegrees() + ", ";
                sql += elset.getMeanAnomaly().getDegrees() + ", ";
                sql += elset.getMeanMotion() + ", ";
                sql += elset.getRevolutionNum();
                sql += ")";

                logger.info("sql = \"" + sql + "\"");
                statement.execute(sql);
            }
            sc.close();
        } catch (Exception exp) {
            logger.error("****** Error " + exp);
        }

        logger.info("count = " + count);
    }

    /**
     * Retrieves the number of element set in the TLEDB database.
     *
     * @return
     * @throws Exception
     */
    public int getElsetCount() throws Exception {

        logger.info("getElsetCount");
        if (conn == null) {
            open();
        }

        String sql = "select count(*) from tledb";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);
        result.next();

        String countStr = result.getString("1");
        return Integer.parseInt(countStr);
    }

    /**
     * Retrieves all element sets from database.
     *
     * @return A List of element sets.
     * @throws Exception
     */
    public List<TwoLineElementSet> getElsets() throws Exception {

        int count = 0;
        logger.info("getElsets:");
        if (conn == null) {
            open();
        }
        List<TwoLineElementSet> elsets = new ArrayList<>();
        Statement statement = conn.createStatement();
        String sql = "SELECT * from TLEDB";
        ResultSet result = statement.executeQuery(sql);
        logger.debug("getElsets: result = " + result);
        while (result.next()) {
            count++;
            TwoLineElementSet elset = parseOneRow(result);
            elsets.add(elset);
        }

        logger.debug("getElsets: count = " + count);
        return elsets;
    }

    /**
     * Retrieves one element set from the database
     *
     * @param satno Element set number to retrieve
     * @return Element set
     * @throws Exception Thrown when requested element set doesn't exist
     */
    public TwoLineElementSet getElset(int satno) throws Exception {

        logger.info("getElset: " + satno);
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();
        String sql = "SELECT * from TLEDB where satelliteNumber = " + satno;
        ResultSet result = statement.executeQuery(sql);
        logger.info("getElset: result = " + result);
        if (result.next()) {
            return parseOneRow(result);
        } else {
            logger.error("getElset: element set not found " + satno);
            throw new Exception("Element set " + satno + " doesn't exist");
        }
    }

    /**
     * Checks to see if the requested satellite number exists in the database.
     *
     * @param satno Input satellite number
     * @return True if the satellite number exists, false otherwise
     * @throws Exception
     */
    public boolean ifElsetExists(int satno) throws Exception {

        logger.info("ifElsetExists: " + satno);
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();
        String sql = "SELECT * from TLEDB where satelliteNumber = " + satno;
        ResultSet result = statement.executeQuery(sql);
        logger.info("ifElsetExists: result = " + result);
        if (result.next()) {
            return true;
        }
        return false;
    }

    /**
     * Gets the database product name (e.g., "Apache Derby")
     * @return The database product name. Returns "Not Connected" is
     *         the database is not connected.
     * @throws SQLException
     */
    public String getDatabaseProductName() throws SQLException {

        if (conn != null) {

            DatabaseMetaData metaData = conn.getMetaData();
            logger.debug("metaData = " + metaData);

            return metaData.getDatabaseProductName();
        }
        return "Not Connected";
    }

    /**
     * Removes all records from the database.
     *
     * @throws Exception
     */
    public void emptyDb() throws Exception {

        logger.info("emptyDb:");
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();
        String sql = "TRUNCATE table TLEDB";
        boolean result = statement.execute(sql);
        logger.debug("emptyDb: result = " + result);
    }

    public void deleteElset(int satno) throws Exception {

        logger.info("deleteElset: satno = " + satno);
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();
        String sql = "DELETE from TLEDB where satelliteNumber = " + satno;
        int result = statement.executeUpdate(sql);
        logger.debug("emptyDb: result = " + result);
    }

    private TwoLineElementSet parseOneRow(ResultSet row) throws SQLException {

        logger.debug("parseOneRow:");
        TwoLineElementSet elset = new TwoLineElementSet();

        elset.setSatelliteNumber(getInt(row, "satelliteNumber"));
        elset.setName(row.getString("name"));
        elset.setClassification(row.getString("classification"));
        elset.setInternationalDesignator(row.getString("internationalDesignator"));
        elset.setEpochYear(getInt(row, "epochYear"));
        elset.setEpochDay(getDouble(row, "epochDay"));
        elset.setMeanMotionDeriv1(getDouble(row, "meanMotionDeriv1"));
        elset.setMeanMotionDeriv2(getDouble(row, "meanMotionDeriv2"));
        elset.setBstar(getDouble(row, "bstar"));
        elset.setElementSetNum(getInt(row, "elementSetNum"));
        elset.setInclination(new Angle(getDouble(row, "inclination"), Angle.AngleUnits.DEGREES));
        elset.setRightAscension(new Angle(getDouble(row, "rightAscension"), Angle.AngleUnits.DEGREES));
        elset.setEccentricity(getDouble(row, "eccentricity"));
        elset.setArgumentOfPerigee(new Angle(getDouble(row, "argumentOfPerigee"), Angle.AngleUnits.DEGREES));
        elset.setMeanAnomaly(new Angle(getDouble(row, "meanAnomaly"), Angle.AngleUnits.DEGREES));
        elset.setMeanMotion(getDouble(row, "meanMotion"));
        elset.setRevolutionNum(getInt(row, "revolutionNum"));

        return elset;
    }

    private int getInt(ResultSet row, String columnName) throws SQLException {
        String str = row.getString(columnName);
        return Integer.parseInt(str);
    }

    private double getDouble(ResultSet row, String columnName) throws SQLException {
        String str = row.getString(columnName);
        return Double.parseDouble(str);
    }

}
