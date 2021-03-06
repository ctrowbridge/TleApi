package com.cindy.tleapi.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Creates the TLEDB database and TLEDB table.
 */
public class CreateDB {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Main app to create the TLEDB.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {

        logger.info("<><><> CreateDB <><><>");

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
