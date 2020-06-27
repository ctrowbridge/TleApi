package com.cindy.tleapi.db;

import com.cindy.tleapi.astro.TwoLineElementSet;

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

    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String databaseURL = "jdbc:derby:scripts/tledb";

    private Connection conn = null;

    /**
     * Connects to the database
     *
     * @throws Exception
     */
    public void open() throws Exception {

        System.out.println("\nOpen database ...");
        String classpathStr = System.getProperty("java.class.path");
        System.out.println("  java.class.path = " + classpathStr);
        Class.forName(driver);

        conn = DriverManager.getConnection(databaseURL);
        System.out.println("  conn = " + conn);
    }

    public void close() throws Exception {
        System.out.println("\nClose database ...");
        if (conn != null) {
            conn.close();
        }
    }

    public void executeSql(String sql) throws Exception {

        Statement statement = conn.createStatement();
        System.out.println("  statement = " + statement);
        statement.execute(sql);
    }

    /**
     * Creates the TLEDB table.
     *
     * @throws Exception
     */
    public void createTable() throws Exception {

        System.out.println("Create database table ...");
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

        System.out.println("*** sql = \"" + sql + "\"");
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

        System.out.println("*** load " + filename);
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

                System.out.println("*** sql = \"" + sql + "\"");
                statement.execute(sql);
            }
        } catch (Exception exp) {
            System.out.println("****** Error " + exp);
        }

    }
}
