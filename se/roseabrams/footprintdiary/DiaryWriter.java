package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.entries.camera.CameraCapture;
import se.roseabrams.footprintdiary.entries.discord.DiscordMessage;
import se.roseabrams.footprintdiary.entries.health.HealthData;
import se.roseabrams.footprintdiary.entries.reddit.RedditComment;
import se.roseabrams.footprintdiary.entries.reddit.RedditPost;
import se.roseabrams.footprintdiary.entries.resfiles.ResFile;
import se.roseabrams.footprintdiary.entries.skype.SkypeMessage;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.entries.steam.SteamStore;
import se.roseabrams.footprintdiary.entries.whatsapp.WhatsAppMessage;
import se.roseabrams.footprintdiary.entries.wikimedia.WikimediaEdit;

public class DiaryWriter {
    private static final String D = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";

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
            d.add(WikimediaEdit.createFromWebsites());
            d.add(RedditPost.createFromCsv(new File(D + "reddit\\posts.csv")));
            d.add(RedditComment.createFromCsv(new File(D + "reddit\\comments.csv")));
            System.gc();

            String csv = d.csv(true);
            Util.writeFile(new File("D:\\Dropbox\\Privat\\postGym program"
                    + "\\footprint diary\\outputs\\diaryTable.csv"), csv);
            System.gc();

            String[] prose = d.prose();
            StringBuilder rtf = new StringBuilder(100000);
            rtf.append("{\\rtf\\ansi\\deff0\\widoctrl\\ftnbj \\sectd\\linex0\\endnhere \\pard\\plain \\fs30 ");
            for (String page : prose) {
                rtf.append(page.replace("\n", "\\par ")).append("\\page ");
            }
            rtf.append("}");
            Util.writeFile(new File("D:\\Dropbox\\Privat\\postGym program"
                    + "\\footprint diary\\outputs\\diaryProse.rtf"), rtf.toString());
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
