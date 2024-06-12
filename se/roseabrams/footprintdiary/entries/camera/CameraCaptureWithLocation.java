package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.GeoLocation;
import se.roseabrams.footprintdiary.interfaces.GeoLocatable;

public class CameraCaptureWithLocation extends CameraCapture implements GeoLocatable {

    public final GeoLocation LOCATION_TAKEN;

    public CameraCaptureWithLocation(DiaryDate date, File file, GeoLocation location) {
        super(date, file);
        assert location != null;
        LOCATION_TAKEN = location;
    }

    @Override
    public GeoLocation geoLocation() {
        return LOCATION_TAKEN;
    }
}