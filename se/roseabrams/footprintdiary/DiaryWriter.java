package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.entries.camera.CameraCapture;
import se.roseabrams.footprintdiary.entries.discord.DiscordMessage;
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

        try {
            d.add(CameraCapture.createFromLog(new File("\\data\\dir Camera.txt")));
            // d.add(CameraCapture.createFromLog(new File("/data/dir Kina.txt")));
            // dating will be harder with mixed modifieddate, maybe there's other metadata?

            d.add(DiscordMessage.createAllFromCsv(new File("\\data\\discord\\messages.csv")));

            d.add(WhatsAppMessage.createAllFromTxt());

            d.add(SpotifyPlayback.createAllFromJson(new File("\\data\\spotify\\endsong_0.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File("\\data\\spotify\\endsong_1.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File("\\data\\spotify\\endsong_2.json")));
            d.add(SpotifyPlaylisting.createFromJson(new File("\\data\\spotify\\Playlist1.json")));

            d.add(ResFile.createFromLog(new File("\\data\\resfiles\\dir.txt")));

            d.add(SteamStore.createFromHtml(new File("\\data\\steam\\Purchase History.html")));
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
