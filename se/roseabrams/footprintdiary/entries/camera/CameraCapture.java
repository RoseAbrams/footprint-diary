package se.roseabrams.footprintdiary.entries.camera;

import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.EntrySource;
import se.roseabrams.footprintdiary.interfaces.LocalResource;

public abstract class CameraCapture extends DiaryEntry implements LocalResource {

    public CameraCapture(short year, byte month, byte day, byte hour, byte minute, byte second) {
        super(EntrySource.CAMERA, year, month, day, hour, minute, second);
    }
}
