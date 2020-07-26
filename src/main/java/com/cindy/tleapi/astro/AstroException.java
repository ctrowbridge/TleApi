package com.cindy.tleapi.astro;

/**
 * Exception class for Astro package.
 */
public class AstroException extends Exception {

    private int id = 0;
    private String description = "";

    public AstroException(int id, String desc) {
        this.id = id;
        this.description = desc;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return "AstroException:  id = " + id + ": " + description;
    }
}
