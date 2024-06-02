package se.roseabrams.footprintdiary.entries.resfiles;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.Picture;

public class ResPicture extends ResFile implements Picture {

    public ResPicture(DiaryEntryCategory source, DiaryDateTime dd, File file) {
        super(source, dd, file);
    }
}
