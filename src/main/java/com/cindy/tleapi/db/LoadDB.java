package com.cindy.tleapi.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Opens, clears and populates the TLEDB Database.
 */
public class LoadDB {

    private static final Logger logger = LogManager.getLogger();
    private static final String defaultFileName = "data\\2019-006.txt";

    /**
     * Main app to load the TLEDB database. The database is cleared
     * before loading.
     *
     * @param args Input command line arguments. If present, args[0] is
     *             the input file name.  If not present, the file
     *             "data\\2019-006.txt" is used.
     */
    public static void main(String[] args) {

        logger.info("<><><> LoadTleDB <><><>");
        logger.info("args.length = " + args.length);
        String fileName = defaultFileName;
        if (args.length > 0) {
            fileName = args[0];
        }
        logger.info("fileName = \"" + fileName + "\"");

        TleDb db = new TleDb();

        try {
            db.open();
            db.emptyDb();
            db.load(fileName);
            int count = db.getElsetCount();
            db.close();

            logger.info(" Database was created and loaded with " + count
            + " element sets");

        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }
}
