package se.roseabrams.footprintdiary.entries.camera;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
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
import se.roseabrams.footprintdiary.common.GeoLocation3D;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.ContentType;
import se.roseabrams.footprintdiary.common.LocalContent;

public class CameraCapture extends DiaryEntry implements ContentContainer {

    public final LocalContent FILE;

    public CameraCapture(DiaryDate date, File file) {
        super(DiaryEntryCategory.CAMERA, date);
        FILE = new LocalContent(file);
    }

    @Override
    public String getStringSummary() {
        return FILE.getName();
    }

    @Override
    public Content getContent() {
        return FILE;
    }

    public static List<CameraCapture> createFromFiles(File folder) throws IOException {
        ArrayList<CameraCapture> output = createFromFilesRecursion(folder);
        return output;
    }

    // i could make generic for all kinds of file collection
    public static ArrayList<CameraCapture> createFromFilesRecursion(File folder) throws IOException {
        ArrayList<CameraCapture> output = new ArrayList<>(5000);
        for (File file : folder.listFiles()) {
            DiaryDateTime modifiedDate = new DiaryDateTime(file.lastModified());
            String filetypeS = file.getName().substring(file.getName().lastIndexOf(".") + 1);

            DiaryDateTime filenameDate;
            try {
                filenameDate = new DiaryDateTime(file.getName());
            } catch (RuntimeException e) {
                filenameDate = null;
            }

            ContentType filetype = ContentType.parseExtension(filetypeS);
            if (filetype == ContentType.SYSTEM)
                continue;
            assert filetype != null && (filetype == ContentType.PICTURE || filetype == ContentType.VIDEO);

            GeoLocation location = null;
            DiaryDateTime exifDate = null;
            if (filetype == ContentType.PICTURE && /* why?? */!filetypeS.equals("heic")) {
                /// https://github.com/apache/commons-imaging/blob/master/src/test/java/org/apache/commons/imaging/examples/MetadataExample.java
                ImageMetadata metadata;
                try {
                    metadata = Imaging.getMetadata(file);
                } catch (ImagingException e) {
                    System.err.println("Error getting metadata of file \"" + file + "\" due to: " + e.getMessage());
                    metadata = null;
                }
                if (metadata != null && metadata instanceof JpegImageMetadata) {
                    TiffImageMetadata exif = ((JpegImageMetadata) metadata).getExif();
                    String[] exifDateO1 = (String[]) exif.getFieldValue(TiffTagConstants.TIFF_TAG_DATE_TIME);
                    String[] exifDateO2 = (String[]) exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                    if (exifDateO1 != null)
                        if (exifDateO2 != null && !exifDateO1[0].equals(exifDateO2[0]))
                            exifDate = new DiaryDateTime(exifDateO2[0]);
                        else
                            exifDate = new DiaryDateTime(exifDateO1[0]);
                    GpsInfo coords = exif.getGpsInfo();
                    if (coords != null) {
                        double latitude = coords.getLatitudeAsDegreesNorth();
                        double longitude = coords.getLongitudeAsDegreesEast();
                        RationalNumber altitudeO = (RationalNumber) exif
                                .getFieldValue(GpsTagConstants.GPS_TAG_GPS_ALTITUDE);
                        if (altitudeO != null) {
                            float altitude = altitudeO.floatValue();
                            location = new GeoLocation3D(latitude, longitude, altitude);
                        } else {
                            location = new GeoLocation(latitude, longitude);
                        }
                    }
                }
            }

            CameraCapture c;
            DiaryDateTime bestDate;
            if (filenameDate != null)
                bestDate = filenameDate;
            else if (exifDate != null)
                bestDate = exifDate;
            else
                bestDate = modifiedDate;

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
     * @Override
     * public StringBuilder detailedCsv(StringBuilder s, String delim) {
     * return s.append(FILE.getName());
     * }
     */
}
