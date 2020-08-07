package com.cindy.tleapi.astro;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Encapsulates an Epoch for an element set.
 */
public class Epoch {

    private Date date;

    public Epoch() {

        date = new Date();
    }

    /**
     * Constructs an Epoch given year and days
     * @param year Year (YYYY)
     * @param days Days (DDD.DDDDDDDDD)
     */
    public Epoch(int year, double days) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        int daysInt = (int)days;
        c.set(Calendar.DAY_OF_YEAR, daysInt);

        double fractDay = days - daysInt;
        double hours = fractDay*24.0f;
        int hoursInt = (int)hours;
        c.set(Calendar.HOUR, hoursInt);

        double fractHours = hours - hoursInt;
        double mins = fractHours*60.0f;
        int minsInt = (int)mins;
        c.set(Calendar.MINUTE, minsInt);

        double minsFract = mins - minsInt;
        double secs = minsFract*60.0f;
        int secsInt = (int)secs;
        c.set(Calendar.SECOND, secsInt);

        double secsFract = secs - secsInt;
        double ms = secsFract*1000.0f;
        int msInt = (int)ms;
        c.set(Calendar.MILLISECOND, msInt);

        date = c.getTime();
    }

    public Date getDate() {

        return date;
    }

    @Override
    public String toString() {

        // 2020-08-03T11:45:40.999968
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS");
        return formatter.format(date);
    }
}
