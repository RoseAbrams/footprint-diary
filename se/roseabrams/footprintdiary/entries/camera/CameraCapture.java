package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.content.Content;
import se.roseabrams.footprintdiary.content.ContentType;
import se.roseabrams.footprintdiary.content.LocalContent;
import se.roseabrams.footprintdiary.interfaces.ContentOwner;

// TODO add GeoLocation but only when available
public class CameraCapture extends DiaryEntry implements ContentOwner {

    public final LocalContent C;

    public CameraCapture(DiaryDate date, File file) {
        super(DiaryEntryCategory.CAMERA, date);
        C = new LocalContent(file);
    }

    @Override
    public String getStringSummary() {
        return C.getName();
    }

    @Override
    public Content getContent() {
        return C;
    }

    public static CameraCapture[] createFromFiles(File folder) {
        ArrayList<CameraCapture> output = createFromFilesRecursion(folder);
        return output.toArray(new CameraCapture[output.size()]);
    }

    public static ArrayList<CameraCapture> createFromFilesRecursion(File folder) {
        ArrayList<CameraCapture> output = new ArrayList<>(1000);
        for (File file : folder.listFiles()) {
            DiaryDateTime date = new DiaryDateTime(file.lastModified());
            String filetype = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            CameraCapture c;
            switch (ContentType.parseExtension(filetype)) {
                case PICTURE:
                case VIDEO:
                    c = new CameraCapture(date, file);
                    break;
                case SYSTEM:
                    continue;
                default:
                    throw new UnsupportedOperationException("Unrecognized filetype: " + filetype);
            }
            output.add(c);
        }
        return output;
    }

    @Deprecated // old and unused
    public static CameraCapture[] createFromLog(File logfile) throws IOException {
        ArrayList<CameraCapture> output = new ArrayList<>(10000);
        Scanner s = new Scanner(logfile);
        while (s.hasNextLine()) {
            Scanner s2 = new Scanner(s.nextLine());
            s2.useDelimiter(":");
            String filename = s2.next();
            long lastModified = s2.nextLong();

            File file = new File("D:\\Dropbox\\Camera Uploads", filename); // won't work for Kina folder
            DiaryDateTime date = new DiaryDateTime(lastModified);
            String filetype = filename.substring(filename.lastIndexOf(".") + 1);

            CameraCapture i;
            switch (ContentType.parseExtension(filetype)) {
                case PICTURE:
                case VIDEO:
                    i = new CameraCapture(date, file);
                    break;
                case SYSTEM:
                    continue;
                default:
                    s2.close();
                    throw new UnsupportedOperationException("Unrecognized filetype: " + filetype);
            }
            output.add(i);
            s2.close();
        }
        s.close();
        return output.toArray(new CameraCapture[output.size()]);
    }
/*
    @Override
    public StringBuilder detailedCsv(StringBuilder s, String delim) {
        return s.append(FILE.getName());
    }*/
}
