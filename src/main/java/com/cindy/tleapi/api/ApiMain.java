package com.cindy.tleapi.api;

import com.cindy.tleapi.astro.TwoLineElementSet;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class for the TLE API.
 *
 * Reference:
 *   https://javalin.io/
 */
public class ApiMain {

    public static int port = 8981;
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        logger.info("ApiMain:  starting Javalin server on port " + port + " ...");
        Javalin app = Javalin.create().start(port);
        app.get("", ctx -> {
            ctx.result("Starting to create the API ...");
        });

    }
}
