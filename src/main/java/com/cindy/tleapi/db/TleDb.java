package com.cindy.tleapi.db;

import com.cindy.tleapi.astro.Angle;
import com.cindy.tleapi.astro.AstroException;
import com.cindy.tleapi.astro.TwoLineElementSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Wrapper for the TLEDB. Opens, creates, loads, updates and clears the TLE database.
 */
public class TleDb {

    static final Logger logger = LogManager.getLogger();

    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String databaseURL = "jdbc:derby:scripts/tledb";

    private Connection conn = null;

    /**
     * Connects to the database.
     *
     * @throws Exception thrown if an error occurs while connection to database
     */
    public void open() throws Exception {

        logger.info("Open database ...");
        conn = DriverManager.getConnection(databaseURL);
        logger.info("  conn = " + conn);
    }

    /**
     * Closes the database.
     *
     * @throws Exception thrown if an error occurs while closing database
     */
    public void close() throws Exception {
        logger.info("Close database ...");
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

    /**
     * Checks to see if database is closed.
     *
     * @return true if closed, false otherwise
     * @throws SQLException thrown if an error occurs check to see if connection is closed
     */
    public boolean isClosed() throws SQLException {
        logger.info("Is database closed? ...");
        if (conn == null) {
            return true;
        }
        return conn.isClosed();
    }

    private void executeSql(String sql) throws Exception {

        Statement statement = conn.createStatement();
        logger.info("Execute statement= " + statement);
        statement.execute(sql);
    }

    /**
     * Creates the TLEDB table.
     *
     * @throws Exception thrown if an error occurs create the TLEDB table.
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
     * @throws Exception thrown if an error occurs loading the database
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
            logger.info("inFile path = " + inFile.getAbsolutePath());
            Scanner sc = new Scanner(inFile);

            while (sc.hasNextLine()) {
                String line0 = sc.nextLine();
                String line1 = sc.nextLine();
                String line2 = sc.nextLine();
                count++;
                TwoLineElementSet elset = new TwoLineElementSet();
                elset.importElset(line0, line1, line2);

                String sql = getAddSql(elset);
                logger.info("sql = \"" + sql + "\"");
                statement.execute(sql);
            }
            sc.close();
        } catch (Exception exp) {
            logger.error("****** Error " + exp);
        }

        statement.close();
        logger.info("count = " + count);
    }

    /**
     * Retrieves the number of element sets in the TLEDB database.
     *
     * @return the number of element sets in the TLEDB database
     * @throws Exception thrown if an error occurs retrieving the element set count
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
        statement.close();
        return Integer.parseInt(countStr);
    }

    /**
     * Retrieves all element sets from database.
     *
     * @return A List of element sets.
     * @throws Exception thrown if an error occurs retrieving element sets from the database
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

        statement.close();
        logger.debug("getElsets: count = " + count);
        return elsets;
    }

    /**
     * Retrieves one element set from the database.
     *
     * @param satno Element set number to retrieve
     * @return Element set
     * @throws Exception Thrown when an error occurs retrieving element set
     * @throws AstroException Thrown when requested element set doesn't exist
     */
    public TwoLineElementSet getElset(int satno) throws Exception, AstroException {

        logger.info("getElset: " + satno);
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();
        String sql = "SELECT * from TLEDB where satelliteNumber = " + satno;
        ResultSet result = statement.executeQuery(sql);
        logger.info("getElset: result = " + result);
        if (result.next()) {
            statement.close();
            return parseOneRow(result);
        } else {
            statement.close();
            logger.error("getElset: element set not found " + satno);
            throw new AstroException(2, "Element set " + satno + " doesn't exist");
        }
    }

    /**
     * Checks to see if the requested satellite number exists in the database.
     *
     * @param satno Input satellite number
     * @return True if the satellite number exists, false otherwise
     * @throws Exception thrown if an error occurs
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

        if (result.isClosed()) {
            statement.close();
            return false;
        }
        if (result.next()) {
            statement.close();
            return true;
        }
        return false;
    }

    /**
     * Gets the database product name (e.g., "Apache Derby").
     *
     * @return The database product name. Returns "Not Connected" is
     *         the database is not connected.
     * @throws SQLException Thrown if an error occurs retrieve metadata.
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
     * @throws Exception thrown when trying to empty database
     */
    public void emptyDb() throws Exception {

        logger.info("emptyDb:");
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();
        String sql = "TRUNCATE table TLEDB";
        boolean result = statement.execute(sql);
        statement.close();
        logger.debug("emptyDb: result = " + result);
    }

    /**
     * Delets one element set from the database.
     *
     * @param satno element set number to delete
     * @throws Exception thrown if an error occurs deleting element set
     */
    public void deleteElset(int satno) throws Exception {

        logger.info("deleteElset: satno = " + satno);
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();
        String sql = "DELETE from TLEDB where satelliteNumber = " + satno;
        int result = statement.executeUpdate(sql);
        statement.close();
        logger.debug("emptyDb: result = " + result);
    }

    /**
     * Adds an element set to the database.
     *
     * @param elset Element set to add to the database
     * @throws Exception thrown if an error occurs in database
     */
    public void addElset(TwoLineElementSet elset) throws Exception {

        logger.info("addElset: elset = " + elset);
        if (conn == null) {
            open();
        }
        Statement statement = conn.createStatement();
        String sql = getAddSql(elset);
        logger.info("sql = \"" + sql + "\"");
        statement.execute(sql);
        statement.close();
        logger.debug("addElset: ");
    }

    private String getAddSql(TwoLineElementSet elset) {

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

        return sql;
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
