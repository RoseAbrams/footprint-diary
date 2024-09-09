package se.roseabrams.footprintdiary.common;

import java.io.Serializable;

public class GeoLocation implements Serializable {

    public final double LATITUDE;
    public final double LONGITUDE;

    public GeoLocation(double latitude, double longitude) {
        LATITUDE = latitude;
        LONGITUDE = longitude;
    }

    public String toString() {
        return String.valueOf(LATITUDE).substring(0, 8) + "° N "
                + String.valueOf(LONGITUDE).substring(0, 8) + "° E";
    }
}
