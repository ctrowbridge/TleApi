package com.cindy.tleapi.api;

import com.cindy.tleapi.astro.AstroException;
import com.cindy.tleapi.astro.TwoLineElementSet;
import com.cindy.tleapi.db.TleDb;
import io.javalin.http.Handler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Handlers for ApiMain
 */
public class ElsetController {

    private static List<TwoLineElementSet> elsets;
    private static final Logger logger = LogManager.getLogger();

    /**
     * Loads data from database
     *
     * @throws Exception
     */
    public static void loadData() throws Exception {

        logger.info("ElsetController::loadData:");

        TleDb tledb = new TleDb();
        elsets = tledb.getElsets();
        tledb.close();

        logger.info("ElsetController::loadData:  elsets loaded, count = " + elsets.size());
    }

    /**
     * Handler for the DELETE route
     */
    public static Handler deleteOneElset = ctx -> {

        String resultStr = "";
        int statusCode = 0;

        int satno = Integer.parseInt(ctx.pathParam("satno"));
        logger.info("ElsetController::deleteOneElset: satno = " + satno);

        TleDb tledb = new TleDb();
        tledb.open();
        boolean exists = tledb.ifElsetExists(satno);
        if (!exists) {

            resultStr = "element set " + satno + " doesn't exist";
            statusCode = 404;
            logger.error(resultStr);
            ctx.result(resultStr).status(statusCode);
            tledb.close();
            logger.info("ElsetController::deleteOneElset: satno not found!");
            return;
        }

        logger.info("ElsetController::deleteOneElset: delete satellite ...");
        tledb.deleteElset(satno);
        tledb.close();
        resultStr = "element set " + satno + " deleted";
        statusCode = 200;

        ctx.result(resultStr).status(statusCode);
    };

    /**
     * Handler for the POST route
     */
    public static Handler postElset = ctx -> {

        logger.info("ElsetController::postElset: post called");

        String line1 = ctx.queryParam("line1");
        String line2 = ctx.queryParam("line2");
        String line3 = ctx.queryParam("line3");

        String msg = checkLines(line1, line2, line3);
        if (msg != null) {
            ctx.result(msg).status(404);
            return;
        }

        String resultStr = "";
        int statusCode = 0;

        TleDb tledb = new TleDb();
        tledb.open();

        TwoLineElementSet postElementSet = new TwoLineElementSet();
        try {
            postElementSet.importElset(line1, line2, line3);
            logger.info("ElsetController::postElset:  postElementSet = " + postElementSet);

            boolean exists = tledb.ifElsetExists(postElementSet.getSatelliteNumber());
            logger.info("ElsetController::postElset:  exists = " + exists);

            if (!exists) {
                logger.info("ElsetController::postElset:  adding elset ...");
                tledb.addElset(postElementSet);
                resultStr = "elset " + postElementSet.getSatelliteNumber() + " added";
                statusCode = 200;
            } else {
                logger.info("ElsetController::postElset:  elset already exists ...");
                resultStr = "elset " + postElementSet.getSatelliteNumber() + " already exists!";
                statusCode = 400;
            }
            tledb.close();

        } catch (AstroException exp) {

            logger.info("ElsetController::postElset:  AstroException - " + exp.getId() + " - " +
                    exp.getDescription());

            resultStr = exp.getDescription();
            statusCode = 404;
            tledb.close();
        }

        logger.info("ElsetController::postElset:  resultStr = " + resultStr + ", statusCode = " + statusCode);
        ctx.result(resultStr).status(statusCode);

    };

    private static String checkLines(String line1, String line2, String line3) {

        logger.info("ElsetController::postElset:  line1 = \"" + line1 + "\"");
        logger.info("ElsetController::postElset:  line2 = \"" + line2 + "\"");
        logger.info("ElsetController::postElset:  line3 = \"" + line3 + "\"");

        if (line1 == null) {
            logger.info("ElsetController::postElset:  line1 is null");
            return "line1 is null";
        }
        if (line2 == null) {
            logger.info("ElsetController::postElset:  line2 is null");
            return "line2 is null";
        }
        if (line3 == null) {
            logger.info("ElsetController::postElset:  line3 is null");
            return "line3 is null";
        }
        return null;
    }

    /**
     * Handler for GET (all elsets)
     */
    public static Handler getElset = ctx -> {

        logger.info("ElsetController::getElset:  get called");

        String resultStr = formatElsets(elsets);
        ctx.contentType("application/json");
        ctx.header("Access-Control-Allow-Origin",
                "*");
        ctx.header("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, ContentType, Accept");
        ctx.result(resultStr);
    };

    private static String formatElsets(List<TwoLineElementSet> elsets) {

        String resultStr = "[\n\t";
        for (TwoLineElementSet elset : elsets) {
            resultStr = resultStr + elset.toJson() + ",\n";
        }
        resultStr = resultStr.substring(0, resultStr.length()-2);
        resultStr = resultStr + "\n]";
        return resultStr;
    }

    /**
     * Handler for GET (with parameters)
     */
    public static Handler getElsetWithParameters = ctx -> {

        logger.info("ElsetController::getElsetWithParameters:  get called");

        int satno = -1;
        try {
            int statusCode = 200;
            boolean found = false;
            satno = Integer.parseInt(ctx.pathParam("satno"));
            logger.info("ElsetController::getElsetWithParameters:  satno = " + satno);

            String resultStr = "";
            for (TwoLineElementSet elset : elsets) {
                if (elset.getSatelliteNumber() == satno) {
                    resultStr = resultStr + elset.toJson();
                    found = true;
                    logger.info("ElsetController::getElsetWithParameters:  found = " + found + " " + resultStr);
                    break;
                }
            }

            if (!found) {
                logger.info("ElsetController::getElsetWithParameters:  not found = " + found);

                resultStr = "Satellite number not found";
                statusCode = 404;
            }
            logger.info("ElsetController::getElsetWithParameters:  statusCode = " + statusCode);
            ctx.contentType("application/json");
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Headers",
                    "Origin, X-Requested-With, ContentType, Accept");
            ctx.result(resultStr).status(statusCode);

        } catch (Exception exp) {
            String resultStr = "satno parameter not an integer ";
            resultStr += exp.getMessage();
            ctx.result(resultStr).status(400);
        }
    };
}
