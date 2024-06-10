package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata.GpsInfo;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.GeoLocation;
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

    public static CameraCapture[] createFromFiles(File folder) throws IOException {
        ArrayList<CameraCapture> output = createFromFilesRecursion(folder);
        return output.toArray(new CameraCapture[output.size()]);
    }

    public static ArrayList<CameraCapture> createFromFilesRecursion(File folder) throws IOException {
        ArrayList<CameraCapture> output = new ArrayList<>(1000);
        for (File file : folder.listFiles()) {
            DiaryDateTime date = new DiaryDateTime(file.lastModified()); // TODO prefer exif date if available
            String filetypeS = file.getName().substring(file.getName().lastIndexOf(".") + 1);

            GeoLocation location = null;
            ImageMetadata metadata = Imaging.getMetadata(file);
            if (metadata instanceof JpegImageMetadata) {
                TiffImageMetadata exif = ((JpegImageMetadata) metadata).getExif();
                GpsInfo coords = exif.getGpsInfo();
                if (coords != null) {
                    double latitude = coords.getLatitudeAsDegreesNorth();
                    double longitude = coords.getLongitudeAsDegreesEast();
                    Object altitudeO = exif.getFieldValue(GpsTagConstants.GPS_TAG_GPS_ALTITUDE);
                    double altitude = (double) altitudeO;
                    location = new GeoLocation(latitude, longitude, altitude);
                }
            }

            ContentType filetype = ContentType.parseExtension(filetypeS);
            if (filetype != ContentType.PICTURE || filetype != ContentType.VIDEO) {
                if (filetype == ContentType.SYSTEM)
                    continue;
                else
                    throw new UnsupportedOperationException("Unrecognized filetype: " + filetype);
            }

            CameraCapture c;
            if (location == null) {
                c = new CameraCapture(date, file);
            } else {
                c = new CameraCaptureWithLocation(date, file, location);
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
     * @Override
     * public StringBuilder detailedCsv(StringBuilder s, String delim) {
     * return s.append(FILE.getName());
     * }
     */
}
