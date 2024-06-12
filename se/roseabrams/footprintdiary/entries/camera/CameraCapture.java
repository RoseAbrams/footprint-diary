package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata.GpsInfo;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.common.GeoLocation;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentType;
import se.roseabrams.footprintdiary.common.LocalContent;
import se.roseabrams.footprintdiary.interfaces.ContentOwner;

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

    // TODO make generic for all kinds of file collection?
    public static ArrayList<CameraCapture> createFromFilesRecursion(File folder) throws IOException {
        ArrayList<CameraCapture> output = new ArrayList<>(1000);
        for (File file : folder.listFiles()) {
            DiaryDateTime modifiedDate = new DiaryDateTime(file.lastModified());
            String filetypeS = file.getName().substring(file.getName().lastIndexOf(".") + 1);

            GeoLocation location = null;
            DiaryDateTime exifDate = null;
            /// https://github.com/apache/commons-imaging/blob/master/src/test/java/org/apache/commons/imaging/examples/MetadataExample.java
            ImageMetadata metadata = Imaging.getMetadata(file);
            if (metadata instanceof JpegImageMetadata) {
                TiffImageMetadata exif = ((JpegImageMetadata) metadata).getExif();
                // TODO debug to see what comes out, then pick which one is best
                Object exifDateO1 = exif.getFieldValue(TiffTagConstants.TIFF_TAG_DATE_TIME);
                Object exifDateO2 = exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                if (exifDateO1 != null)
                    exifDate = new DiaryDateTime((String) exifDateO1);
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
            DiaryDateTime bestDate = exifDate != null ? exifDate : modifiedDate;
            if (location == null) {
                c = new CameraCapture(bestDate, file);
            } else {
                c = new CameraCaptureWithLocation(bestDate, file, location);
            }
            output.add(c);
        }
        return output;
    }
    /*
     * @Deprecated // old and unused
     * public static CameraCapture[] createFromLog(File logfile) throws IOException
     * {
     * ArrayList<CameraCapture> output = new ArrayList<>(10000);
     * Scanner s = new Scanner(logfile);
     * while (s.hasNextLine()) {
     * Scanner s2 = new Scanner(s.nextLine());
     * s2.useDelimiter(":");
     * String filename = s2.next();
     * long lastModified = s2.nextLong();
     * 
     * File file = new File("D:\\Dropbox\\Camera Uploads", filename); // won't work
     * for Kina folder
     * DiaryDateTime date = new DiaryDateTime(lastModified);
     * String filetype = filename.substring(filename.lastIndexOf(".") + 1);
     * 
     * CameraCapture i;
     * switch (ContentType.parseExtension(filetype)) {
     * case PICTURE:
     * case VIDEO:
     * i = new CameraCapture(date, file);
     * break;
     * case SYSTEM:
     * continue;
     * default:
     * s2.close();
     * throw new UnsupportedOperationException("Unrecognized filetype: " +
     * filetype);
     * }
     * output.add(i);
     * s2.close();
     * }
     * s.close();
     * return output.toArray(new CameraCapture[output.size()]);
     * }
     */
    /*
     * @Override
     * public StringBuilder detailedCsv(StringBuilder s, String delim) {
     * return s.append(FILE.getName());
     * }
     */
}
