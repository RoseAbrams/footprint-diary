package se.roseabrams.footprintdiary.entries.resfiles;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntrySource;

public class Torrent extends ResFile {

    public Torrent(DiaryDateTime dd, File file) {
        super(DiaryEntrySource.TORRENT, dd, file);
    }
}
