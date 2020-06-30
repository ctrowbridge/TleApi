package com.cindy.tleapi.db;

import com.cindy.tleapi.astro.TwoLineElementSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ReadDB {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        logger.info("<><><> ReadDB <><><>");

        TleDb db = new TleDb();

        try {
            db.open();

            String databaseName = db.getDatabaseProductName();
            logger.info("databaseName = \"" + databaseName + "\"");

            int count = db.getElsetCount();
            logger.info("count = " + count);

            List<TwoLineElementSet> elsets = db.getElsets();
            for (TwoLineElementSet elset : elsets) {
                logger.info("elset = " + elset);
            }
            db.close();

        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }
}
