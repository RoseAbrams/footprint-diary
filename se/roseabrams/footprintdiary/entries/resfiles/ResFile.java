package se.roseabrams.footprintdiary.entries.resfiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.interfaces.LocalResource;

public abstract class ResFile extends DiaryEntry implements LocalResource {
    public final File FILE;

    public ResFile(DiaryEntrySource source, DiaryDateTime dd, File file) {
        super(source, dd);
        assert source == DiaryEntrySource.MEME_SAVED || source == DiaryEntrySource.MEME_CREATED
                || source == DiaryEntrySource.WALLPAPER_SAVED || source == DiaryEntrySource.OTHER_MEMESQUE_SAVED;
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

    public static ResFile[] createFromLog(File logfile) throws IOException {
        ArrayList<ResFile> output = new ArrayList<>();
        Scanner s = new Scanner(logfile);
        while (s.hasNextLine()) {
            // ...
            DiaryEntrySource s;
            switch (subfolderName) {
                case "lol":
                    s = DiaryEntrySource.MEME_SAVED;
                    break;
                case "oc":
                    s = DiaryEntrySource.MEME_CREATED;
                    break;
                case "wp":
                    s = DiaryEntrySource.WALLPAPER_SAVED;
                    break;
                default:
                    s = DiaryEntrySource.OTHER_MEMESQUE_SAVED;
                    break;
            }
            // ...
            ResFile r;
            switch (filetype.toLowerCase()) {
                case "jpg":
                case "jpeg":
                case "png":
                case "webp":
                    r = new ResPicture();
                    break;
                case "mp4":
                case "mov":
                case "webm":
                    r = new ResVideo();
                    break;
                case "gif":
                    r = new ResGif();
                    break;
                default:
                    //throw new UnsupportedOperationException("Unrecognized filename: " + filetype);
                    System.err.println("Unrecognized filetype: " + filetype);
                    break;
            }
            // ...
        }
        s.close();
        return output.toArray(new ResFile[output.size()]);
    }
}
