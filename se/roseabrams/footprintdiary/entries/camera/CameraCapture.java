package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.LocalResource;

public abstract class CameraCapture extends DiaryEntry implements LocalResource {

    public final File FILE;

    public CameraCapture(DiaryDate date, File file) {
        super(DiaryEntryCategory.CAMERA, date);
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
            switch (filetype.toLowerCase()) {
                case "jpg":
                case "jpeg":
                case "heic":
                case "png":
                case "webp":
                    c = new CameraPicture(date, file);
                    break;
                case "mov":
                case "mp4":
                case "avi":
                    c = new CameraVideo(date, file);
                    break;
                /*
                 * // apparently not a reliable determiner
                 * case "png":
                 * case "webp":
                 * i = new Screenshot(date, file);
                 * break;
                 * case "mp4":
                 * i = new ScreenRecording(date, file);
                 * break;
                 */
                case "ini":
                    continue;
                default:
                    throw new UnsupportedOperationException("Unrecognized filetype: " + filetype);
            }
            output.add(c);
        }
        return output;
    }

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
            switch (filetype) {
                case "jpg":
                case "jpeg":
                case "heic":
                case "png":
                case "webp":
                    i = new CameraPicture(date, file);
                    break;
                case "mov":
                case "mp4":
                    i = new CameraVideo(date, file);
                    break;
                /*
                 * // apparently not a reliable determiner
                 * case "png":
                 * case "webp":
                 * i = new Screenshot(date, file);
                 * break;
                 * case "mp4":
                 * i = new ScreenRecording(date, file);
                 * break;
                 */
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
}
