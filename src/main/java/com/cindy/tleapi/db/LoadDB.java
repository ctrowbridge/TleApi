package com.cindy.tleapi.db;

/**
 * Opens and populates the TLEDB Database.
 */
public class LoadDB {

    public static void main(String[] args) {

        System.out.println("<><><> LoadTleDB <><><>");

        TleDb db = new TleDb();

        try {
            db.open();
            db.load("data\\2019-006.txt");
            db.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
