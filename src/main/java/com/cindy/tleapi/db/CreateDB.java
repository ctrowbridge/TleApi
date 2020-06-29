package com.cindy.tleapi.db;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates the TLEDB database and TLEDB table.
 */
public class CreateDB {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        logger.info("<><><> LoadTleDB i <><><>");

        TleDb db = new TleDb();

        try {
            db.open();
            db.createTable();
            db.close();

        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }
}
