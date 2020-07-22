package com.cindy.tleapi.api;

import com.cindy.tleapi.astro.TwoLineElementSet;
import com.cindy.tleapi.db.TleDb;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Main class for the TLE API. The API is implemented using Javalin.
 *
 * Reference:
 *   https://javalin.io/
 */
public class ApiMain {

    public static int port = 8981;
    public static Javalin app;

    private static List<TwoLineElementSet> elsets;
    private static final Logger logger = LogManager.getLogger();

    /**
     * Main app for the API.
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            loadData();
            createServer(port);
            createRoutes();

        } catch (Exception exp) {
            logger.error("****** Error " + exp);
        }
    }

    /**
     * Loads data from the TLE DB.
     *
     * @throws Exception
     */
    private static void loadData() throws Exception {

        logger.info("ApiMain::loadData:");

        TleDb tledb = new TleDb();
        elsets = tledb.getElsets();
        tledb.close();

        logger.info("Api::main:  elsets loaded, count = " + elsets.size());
    }

    /**
     * Creates the Javalin server on the given port.
     *
     * @param port Port to use for the server
     */
    private static void createServer(int port) {

        logger.info("ApiMain::createServer:  starting Javalin server on port " + port + " ...");
        app = Javalin.create().start(port);
    }

    private static void createRoutes() {

        logger.info("ApiMain::createRoutes:");

        app.get("", ctx -> {
            ctx.result("Starting to create the API ...");
        });

        createGetRoute();
        createGetRouteWithParameter();
        createPostRoute();
    }

    private static void createPostRoute() {

        logger.info("ApiMain::createPostRoute:");

        //app.post("/elsets/:line1/:line2/:line3", ctx -> {
        app.post("/elsets/", ctx -> {

            logger.info("ApiMain::createPostRoute: post called");

            String line1 = ctx.queryParam("line1");
            String line2 = ctx.queryParam("line2");
            String line3 = ctx.queryParam("line3");

            logger.info("ApiMain::createPostRoute:  line1 = \"" + line1 + "\"");
            logger.info("ApiMain::createPostRoute:  line2 = \"" + line2 + "\"");
            logger.info("ApiMain::createPostRoute:  line3 = \"" + line3 + "\"");

            TwoLineElementSet postElementSet = new TwoLineElementSet();
            postElementSet.importElset(line1, line2, line3);
            logger.info("ApiMain::createPostRoute:  postElementSet = " + postElementSet);

            TleDb tledb = new TleDb();
            tledb.open();
            tledb.close();

            ctx.result("TODO").status(200);

        });
    }

    /**
     * Create route to get all elsets.
     */
    private static void createGetRoute() {

        logger.info("ApiMain::createGetRoute:");

        // TODO Replace String with StringBuffer
        app.get("/elsets", ctx -> {

            logger.info("ApiMain::createGetRoute:  get called");

            String resultStr = "[\n\t";
            for (TwoLineElementSet elset : elsets) {
                resultStr = resultStr + elset.toJson() + ",\n";
            }
            resultStr = resultStr.substring(0, resultStr.length()-2);
            resultStr = resultStr + "\n]";
            ctx.contentType("application/json");
            ctx.header("Access-Control-Allow-Origin",
                    "*");
            ctx.header("Access-Control-Allow-Headers",
                    "Origin, X-Requested-With, ContentType, Accept");
            ctx.result(resultStr);
        });
    }

    /**
     * Create route to get one elset
     */
    private static void createGetRouteWithParameter() {

        logger.info("ApiMain::createGetRouteWithParameter:");

        app.get("/elsets/:satno", ctx -> {

            logger.info("ApiMain::createGetRouteWithParameter:  get called");

            int satno = -1;
            try {
                int statusCode = 200;
                boolean found = false;
                satno = Integer.parseInt(ctx.pathParam("satno"));
                logger.info("Api::createRoutes:  satno = " + satno);

                String resultStr = "";
                for (TwoLineElementSet elset : elsets) {
                    if (elset.getSatelliteNumber() == satno) {
                        resultStr = resultStr + elset.toJson();
                        found = true;
                        logger.info("Api::createRoutes:  found = " + found + " " + resultStr);
                        break;
                    }
                }

                if (!found) {
                    logger.info("Api::createRoutes:  not found = " + found);

                    resultStr = "Satellite number not found";
                    statusCode = 404;
                }
                logger.info("Api::createRoutes:  statusCode = " + statusCode);
                ctx.contentType("application/json");
                ctx.header("Access-Control-Allow-Origin", "*");
                ctx.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, ContentType, Accept");
                ctx.result(resultStr).status(statusCode);

            } catch (Exception exp) {
                String resultStr = "satno parameter not an integer ";
                resultStr += exp.getMessage();
                ctx.result(resultStr).status(400);
            }
        });
    }
}
