package se.roseabrams.footprintdiary.entries.resfiles;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.interfaces.Video;

public class ResGif extends ResFile implements Video {

    public ResGif(DiaryEntrySource source, DiaryDateTime dd, File file) {
        super(source, dd, file);
    }
}