package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;

import DiaryDate;
import interfaces.Video;

public class CameraVideo extends CameraCapture implements Video {

    public CameraVideo(DiaryDate date, File file) {
        super(date, file);
    }
}
