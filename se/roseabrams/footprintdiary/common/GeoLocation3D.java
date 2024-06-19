package se.roseabrams.footprintdiary.common;

public class GeoLocation3D extends GeoLocation {

    public final float ALTITUDE;

    public GeoLocation3D(double latitude, double longitude, float altitude) {
        super(latitude, longitude);
        ALTITUDE = altitude;
    }

    public String toString() {
        return super.toString() + String.valueOf(ALTITUDE).substring(0, 5);
    }
}
