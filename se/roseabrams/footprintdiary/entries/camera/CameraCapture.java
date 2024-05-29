package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.interfaces.LocalResource;

public abstract class CameraCapture extends DiaryEntry implements LocalResource {

    public CameraCapture(DiaryDate date) {
        super(DiaryEntrySource.CAMERA, date);
    }

    public static CameraCapture[] create(File logfile) throws IOException {
    }
}
