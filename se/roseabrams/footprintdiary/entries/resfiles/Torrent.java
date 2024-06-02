package se.roseabrams.footprintdiary.entries.resfiles;

import java.io.File;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.LocalResource;

public class Torrent extends DiaryEntry implements LocalResource {

    public final File FILE;

    public Torrent(DiaryDateTime dd, File file) {
        super(DiaryEntryCategory.TORRENT, dd);
        FILE = file;
    }

    @Override
    public String getStringSummary() {
        return FILE.getName();
    }

    @Override
    public String getPathToResource() {
        return FILE.getAbsolutePath();
    }

    @Override
    public File getFileOfResource() {
        return FILE;
    }
}
