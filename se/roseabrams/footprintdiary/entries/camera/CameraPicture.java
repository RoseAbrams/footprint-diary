package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.interfaces.Picture;

public class CameraPicture extends CameraCapture implements Picture {

    public CameraPicture(DiaryDate date, File file) {
        super(date, file);
    }
}
