package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.entries.camera.CameraCapture;
import se.roseabrams.footprintdiary.entries.discord.DiscordMessage;
import se.roseabrams.footprintdiary.entries.health.HealthData;
import se.roseabrams.footprintdiary.entries.resfiles.ResFile;
import se.roseabrams.footprintdiary.entries.skype.SkypeMessage;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.entries.steam.SteamStore;
import se.roseabrams.footprintdiary.entries.whatsapp.WhatsAppMessage;

public class DiaryWriter {
    private static final String D = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";
    private static final File CSV_FILE = new File(
            "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\outputs\\diaryTable.csv");

    public static void main(String[] args) {
        DiaryBook d = new DiaryBook(new DiaryDate((short) 2010, (byte) 1, (byte) 1),
                new DiaryDate((short) 2023, (byte) 3, (byte) 31));

        try {
            d.add(CameraCapture.createFromLog(new File(D + "dir Camera Uploads.csv")));
            d.add(CameraCapture.createFromLog(new File(D + "dir Kina.csv")));
            d.add(ResFile.createFromLog(new File(D + "dir res.csv")));
            d.add(DiscordMessage.createAllFromCsv(new File(D + "discord\\messages.csv")));
            d.add(WhatsAppMessage.createAllFromTxt(new File(D + "whatsapp")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_0.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_1.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_2.json")));
            d.add(SpotifyPlaylisting.createFromJson(new File(D + "spotify\\Playlist1.json")));
            d.add(ResFile.createFromLog(new File(D + "dir res.txt")));
            d.add(SteamStore.createFromHtml(new File(D + "steam\\Purchase History.html")));
            d.add(HealthData.createFromXml(new File(D + "apple\\health.xml")));
            d.add(SkypeMessage.createAllFromTxt(new File(D + "skype")));
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
