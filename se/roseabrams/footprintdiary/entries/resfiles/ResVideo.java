package se.roseabrams.footprintdiary.entries.resfiles;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.Video;

public class ResVideo extends ResFile implements Video {

    public ResVideo(DiaryEntryCategory source, DiaryDateTime dd, File file) {
        super(source, dd, file);
    }
}
