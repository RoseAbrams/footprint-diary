package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.common.GeoLocatable;
import se.roseabrams.footprintdiary.common.GeoLocation;

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
