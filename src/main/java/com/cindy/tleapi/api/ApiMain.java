package com.cindy.tleapi.api;

import com.cindy.tleapi.astro.AstroException;
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

    private static final Logger logger = LogManager.getLogger();

    /**
     * Main app for the API.  Uses Javalin to create an REST API with
     * the following endpoints:  GET, GET with parameters, POST
     *
     * @param args Input arguments. Not currently used.
     */
    public static void main(String[] args) {

        try {
            ElsetController.loadData();
            createServer(port);
            createRoutes();

        } catch (Exception exp) {
            logger.error("****** Error " + exp);
        }
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
        createDeleteRoute();
    }

    private static void createDeleteRoute() {

        logger.info("ApiMain::createDeleteRoute:");

        app.delete("/elsets/:satno", ElsetController.deleteOneElset);
    }

    private static void createPostRoute() {

        logger.info("ApiMain::createPostRoute:");

        app.post("/elsets/", ElsetController.postElset);
    }

    /**
     * Create route to get all elsets.
     */
    private static void createGetRoute() {

        logger.info("ApiMain::createGetRoute:");

        app.get("/elsets", ElsetController.getElset);
    }

    private static void createGetRouteWithParameter() {

        logger.info("ApiMain::createGetRouteWithParameter:");

        app.get("/elsets/:satno", ElsetController.getElsetWithParameters);
    }
}
