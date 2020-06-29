package com.cindy.tleapi.db;

import com.cindy.tleapi.astro.TwoLineElementSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;


/**
 * Opens, creates and loads the TLE database.
 */
public class TleDb {

    static final Logger logger = LogManager.getLogger();

    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String databaseURL = "jdbc:derby:scripts/tledb";

    private Connection conn = null;

    /**
     * Connects to the database
     *
     * @throws Exception
     */
    public void open() throws Exception {

        logger.info("Open database ...");
        conn = DriverManager.getConnection(databaseURL);
        logger.info("  conn = " + conn);
    }

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

        String sql = "CREATE TABLE tledb";
        sql += "(";
        sql += "satelliteNumber int, ";
        sql += "name varchar(30),";
        sql += "classification varchar(1),";
        sql += "internationalDesignator varchar(10),";
        sql += "epochYear int,";
        sql += "epochDay int,";
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
        } catch (Exception exp) {
            logger.error("****** Error " + exp);
        }

    }
}
