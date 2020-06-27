package com.cindy.tleapi.db;

/**
 * Creates the TLEDB database and TLEDB table.
 */
public class CreateDB {

    public static void main(String[] args) {

        System.out.println("<><><> LoadTleDB <><><>");

        TleDb db = new TleDb();

        try {
            db.open();
            db.createTable();
            db.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

}
