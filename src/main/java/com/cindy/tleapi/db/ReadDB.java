package com.cindy.tleapi.db;

import com.cindy.tleapi.astro.TwoLineElementSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * Reads from the TLEDB database
 */
public class ReadDB {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        logger.info("<><><> ReadDB <><><>");

        TleDb db = new TleDb();

        try {
            db.open();

            String databaseName = db.getDatabaseProductName();
            logger.info("databaseName = \"" + databaseName + "\"");

            getElementSetCount(db);

            readAllElementSets(db);

            readOneElementSet(db, 44383);
            
            db.close();

        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    private static void readOneElementSet(TleDb db, int satno) throws Exception {

        logger.info("readOneElementSet:");

        TwoLineElementSet elset = db.getElset(satno);
        logger.info("getElset:  elset = " + elset);
    }

    private static void getElementSetCount(TleDb db) throws Exception {

        logger.info("getElementSetCount:");
        int count = db.getElsetCount();
        logger.info("count = " + count);
    }

    private static void readAllElementSets(TleDb db) throws Exception {

        logger.info("readAllElementSets:");
        String databaseName = db.getDatabaseProductName();
        List<TwoLineElementSet> elsets = db.getElsets();
        for (TwoLineElementSet elset : elsets) {
            logger.info("elset = " + elset);
        }

    }
}
