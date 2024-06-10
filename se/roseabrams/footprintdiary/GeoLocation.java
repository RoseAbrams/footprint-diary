package se.roseabrams.footprintdiary;

public class GeoLocation {

    public final double LATITUDE;
    public final double LONGITUDE;
    public final double ALTITUDE;

    public GeoLocation(double latitude, double longitude, double altitude) {
        LATITUDE = latitude;
        LONGITUDE = longitude;
        ALTITUDE = altitude;
    }

    public String toString() {
        return String.valueOf(LATITUDE).substring(0, 8) + "° N "
                + String.valueOf(LONGITUDE).substring(0, 8) + "° E – altitude "
                + String.valueOf(ALTITUDE).substring(0, 5);
    }
}
