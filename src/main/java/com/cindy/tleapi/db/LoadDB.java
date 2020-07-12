package com.cindy.tleapi.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Opens and populates the TLEDB Database.
 */
public class LoadDB {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        logger.info("<><><> LoadTleDB <><><>");

        TleDb db = new TleDb();

        try {
            db.open();
            db.load("data\\2019-006.txt");
            int count = db.getElsetCount();
            db.close();

            logger.info(" Database is created and loaded with " + count
            + " element sets");

        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }
}
