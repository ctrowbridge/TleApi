package com.cindy.tleapi.astro;

/**
 * Represents one Angle, with units specified.
 */
public class Angle {

    public enum AngleUnits {DEGREES, RADIANS}

    private double valueRad;

    public Angle(double value, AngleUnits units) {
        if (units == AngleUnits.RADIANS) {
            this.valueRad = value;
        } else {
            this.valueRad = Math.toRadians(value);
        }
    }

    public double getDegrees() {
        return Math.toDegrees(valueRad);
    }

    public double getRadians() {
        return valueRad;
    }

    @Override
    public String toString() {
        return "Angle{" +
                "valueRad=" + valueRad +
                '}';
    }
}
