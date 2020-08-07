package com.cindy.tleapi.test;

import com.cindy.tleapi.astro.Epoch;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

public class TestEpoch {

    //ISS (ZARYA)
    //1 25544U 98067A   20216.49005787  .00002416  00000-0  51722-4 0  9995
    //            2 25544  51.6454 114.3468 0000873 353.4400   1.0119 15.49140402239344

    /*
        OBJECT_NAME    = ISS (ZARYA)
        OBJECT_ID      = 1998-067A
        EPOCH          = 2020-08-03T11:45:40.999968
     */
    @Test
    public void testDefaultConstructor() {

        System.out.println("testDefaultConstructor");

        Epoch epoch = new Epoch();
        System.out.println(" epoch1 = " + epoch);
        Date date = epoch.getDate();
        System.out.println(" date = " + date);
    }

    @Test
    public void testConstructor() {

        System.out.println("testConstructor");

        // 1986/050:06:49:30.94
        Epoch epoch = new Epoch(1986, 50.28438588 );

        System.out.println(" epoch = " + epoch);
        Assert.assertEquals(epoch.toString(), "1986-02-19T06:49:30.940");
    }

    @Test
    public void testConstructorFromTwoLine() {

        System.out.println("testConstructorFromTwoLine");

        // 2020-08-03T11:45:40.999968
        Epoch epoch = new Epoch(2020, 216.49005787);
        System.out.println(" epoch = " + epoch);
        Assert.assertEquals(epoch.toString(), "2020-08-03T11:45:40.999");
    }
}
