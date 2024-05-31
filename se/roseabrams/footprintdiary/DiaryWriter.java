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
    private static final String D = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";
    private static final File CSV_FILE = new File(
            "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\outputs\\diaryTable.csv");

    public static void main(String[] args) {
        DiaryBook d = new DiaryBook(new DiaryDate(START_YEAR, (byte) 1, (byte) 1),
                new DiaryDate(END_YEAR, (byte) 12, (byte) 31));

        try {
            d.add(CameraCapture.createFromLog(new File(D + "dir Camera Uploads.txt")));
            // d.add(CameraCapture.createFromLog(new File(D + "dir Kina.txt")));
            // exif tag "date taken" matches with modifieddate🤔
            d.add(DiscordMessage.createAllFromCsv(new File(D + "discord\\messages.csv")));
            d.add(WhatsAppMessage.createAllFromTxt(new File(D + "whatsapp")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_0.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_1.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_2.json")));
            d.add(SpotifyPlaylisting.createFromJson(new File(D + "spotify\\Playlist1.json")));
            d.add(ResFile.createFromLog(new File(D + "dir res.txt")));
            d.add(SteamStore.createFromHtml(new File(D + "steam\\Purchase History.html")));
            d.add(HealthData.createFromXml(new File(D + "apple\\health.xml")));
            System.gc();

            String csv = d.csv(true);
            Util.writeFile(CSV_FILE, csv);
            System.gc();

            String[] prose = d.prose();
            ... // TODO divide into pages, some kind of document format
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
