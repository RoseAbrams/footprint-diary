package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.entries.camera.CameraCapture;
import se.roseabrams.footprintdiary.entries.discord.DiscordMessage;
import se.roseabrams.footprintdiary.entries.health.HealthData;
import se.roseabrams.footprintdiary.entries.resfiles.ResFile;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.entries.steam.SteamStore;
import se.roseabrams.footprintdiary.entries.whatsapp.WhatsAppMessage;

public class DiaryWriter {
    public static final short START_YEAR = 2012;
    public static final short END_YEAR = 2021;

    public static void main(String[] args) {
        DiaryBook d = new DiaryBook(new DiaryDate(START_YEAR, (byte) 1, (byte) 1),
                new DiaryDate(END_YEAR, (byte) 12, (byte) 31));
        String baseDir = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";

        try {
            d.add(CameraCapture.createFromLog(new File(baseDir + "dir Camera Uploads.txt")));
            // d.add(CameraCapture.createFromLog(new File(baseDir + "dir Kina.txt")));
            /*
             * exif tag "date taken" matches with modifieddate🤔
             * added a library for exif anyway, instructions at
             * "https://github.com/apache/commons-imaging/blob/master/src/test/java/org/apache/commons/imaging/examples/MetadataExample.java"
             */

            d.add(DiscordMessage.createAllFromCsv(new File(baseDir + "discord\\messages.csv")));

            d.add(WhatsAppMessage.createAllFromTxt(new File(baseDir + "whatsapp")));

            d.add(SpotifyPlayback.createAllFromJson(new File(baseDir + "spotify\\endsong_0.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(baseDir + "spotify\\endsong_1.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(baseDir + "spotify\\endsong_2.json")));
            d.add(SpotifyPlaylisting.createFromJson(new File(baseDir + "spotify\\Playlist1.json")));

            d.add(ResFile.createFromLog(new File(baseDir + "dir res.txt")));

            d.add(SteamStore.createFromHtml(new File(baseDir + "steam\\Purchase History.html")));

            d.add(HealthData.createFromXml(new File(baseDir + "apple\\health.xml")));
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace(System.err);
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause());
                e.getCause().printStackTrace(System.err);
            }
            System.exit(1);
        }
    }
}
