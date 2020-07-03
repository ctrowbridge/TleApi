package com.cindy.tleapi.api;

import com.cindy.tleapi.astro.TwoLineElementSet;
import com.cindy.tleapi.db.TleDb;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Main class for the TLE API.
 *
 * Reference:
 *   https://javalin.io/
 */
public class ApiMain {

    public static int port = 8981;
    public static Javalin app;

    private static List<TwoLineElementSet> elsets;
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        try {
            loadData();
        } catch (Exception exp) {
            logger.error("****** Error " + exp);
        }
        createServer(port);
        createRoutes();
    }

    private static void loadData() throws Exception {
        logger.info("ApiMain::loadData:");

        TleDb tledb = new TleDb();
        elsets = tledb.getElsets();

        logger.info("Api::main:  elsets loaded, count = " + elsets.size());
    }

    private static void createServer(int port) {

        logger.info("ApiMain::createServer:  starting Javalin server on port " + port + " ...");
        app = Javalin.create().start(port);
    }

    private static void createRoutes() {
        app.get("", ctx -> {
            ctx.result("Starting to create the API ...");
        });

        app.get("/elsets", ctx -> {
            String resultStr = "[\n\t";
            for (TwoLineElementSet elset : elsets) {
                resultStr = resultStr + elset.toJson() + ",\n";
            }
            resultStr = resultStr + "\n]";
            ctx.result(resultStr);
        });
    }

}
