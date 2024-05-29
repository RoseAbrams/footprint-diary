package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntrySource;
import se.roseabrams.footprintdiary.interfaces.LocalResource;

public abstract class CameraCapture extends DiaryEntry implements LocalResource {

    private final File FILE;

    public CameraCapture(DiaryDate date, File file) {
        super(DiaryEntrySource.CAMERA, date);
        FILE = file;
    }

    public static CameraCapture[] create(File logfile) throws IOException {
        Scanner s = new Scanner(logfile);
        ArrayList<CameraCapture> output = new ArrayList<>();
        while (s.hasNextLine()) {
            String filename = s.nextLine();
            File file = new File("D:\\Dropbox\\Camera Uploads", filename);
            DiaryDateTime date = new DiaryDateTime(filename.substring(0, filename.lastIndexOf(".")));
            String filetype = filename.substring(filename.lastIndexOf("."));

            CameraCapture i;
            switch (filetype) {
                case "jpg":
                case "jpeg":
                case "heic":
                    i = new CameraPicture(date, file);
                    break;
                case "mov":
                    i = new CameraVideo(date, file);
                    break;
                case "png":
                    i = new Screenshot(date, file);
                    break;
                case "mp4":
                    i = new ScreenRecording(date, file);
                    break;
                default:
                    throw new UnsupportedOperationException("Unrecognized filename: " + filetype);
            }
            output.add(i);
        }
        s.close();
        return output.toArray(new CameraCapture[output.size()]);
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
