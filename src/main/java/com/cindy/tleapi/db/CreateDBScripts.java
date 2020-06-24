package com.cindy.tleapi.db;

import com.cindy.tleapi.astro.TwoLineElementSet;

import java.io.*;
import java.util.Scanner;

public class CreateDBScripts {

    private static final String connectFileName = "scripts\\connectDB.sql";
    private static final String dbName = "tledb";
    private static final String createSqlFileName = "scripts\\createDB.sql";
    private static final String populateFileName = "scripts\\loadDB.sql";
    private static final String inputFileName = "data\\2019-006.txt";

    public static void main(String[] args) {

        System.out.println("<><><> CreateDBScripts <><><>");

        CreateDBScripts dbScripts = new CreateDBScripts();
        dbScripts.generateScripts();

        System.out.println("Done!");
    }

    private void generateScripts() {

        checkDirectories();
        generateConnectScript();
        generateCreateDbScript();
        generateLoadScript();

    }
    private void checkDirectories() {

        System.out.println("\nCheck directories ...");
        File scriptPath = new File("scripts");
        if (!scriptPath.exists()) {
            System.out.println("Creating scripts directory ...");
            boolean success = scriptPath.mkdirs();
            if (success) {
                System.out.println("scripts directory created!");
            } else {
                System.out.println("Unable to create scripts directory");
                System.exit(1);
            }
        }
    }
    private void generateConnectScript() {

        System.out.println("\nGenerate database connection script ...");

        try {
            File outFile = new File(connectFileName);
            FileWriter fWriter = new FileWriter(outFile);
            PrintWriter pWriter = new PrintWriter(fWriter);

            pWriter.println("-- Script to connect to database");
            pWriter.println("CONNECT 'jdbc:derby:" + dbName + ";create=true';");
            pWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Script created \"" + connectFileName + "\"");
    }

    private void generateCreateDbScript() {

        System.out.println("\nGenerate database creation script ...");

        try {
            File outFile = new File(createSqlFileName);
            FileWriter fWriter = new FileWriter(outFile);
            PrintWriter pWriter = new PrintWriter(fWriter);

            pWriter.println("-- Script to create locations database ");

            pWriter.println("DROP TABLE tledb;");
            pWriter.println("CREATE TABLE tledb");
            pWriter.println("(");
            pWriter.println("    satno int, ");
            pWriter.println("    name varchar(30)");
            pWriter.println(");");

            pWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Script created \"" + createSqlFileName + "\"");
    }

    private void generateLoadScript() {

        System.out.println("\nGenerate database load script ...");

        try {
            File outFile = new File(populateFileName);
            FileWriter fWriter = new FileWriter(outFile);
            PrintWriter pWriter = new PrintWriter(fWriter);

            pWriter.println("-- Script to populate database");

            parseTLEFile(inputFileName, pWriter);

            pWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Script created \"" + populateFileName + "\"");
    }

    private void parseTLEFile(String filename, PrintWriter pWriter) {

        System.out.println("\n Parse TLE File ...");
        try {
            pWriter.println("INSERT INTO " + dbName + " VALUES");

            File inFile = new File(filename);
            Scanner sc = new Scanner(inFile);

            while(sc.hasNextLine()) {
                String line0 = sc.nextLine();
                String line1 = sc.nextLine();
                String line2 = sc.nextLine();

                TwoLineElementSet elset = new TwoLineElementSet();
                elset.importElset(line0, line1, line2);

                pWriter.print("(" + elset.getSatelliteNumber() +
                        ", '" + elset.getName() + "')");

                if (sc.hasNextLine()) {
                    pWriter.println(", ");
                } else {
                    pWriter.println("");
                }
            }
            pWriter.println(";");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
