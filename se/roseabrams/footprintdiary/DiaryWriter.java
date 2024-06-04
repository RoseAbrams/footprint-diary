package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.entries.camera.CameraCapture;
import se.roseabrams.footprintdiary.entries.discord.DiscordMessage;
import se.roseabrams.footprintdiary.entries.health.DailyActivity;
import se.roseabrams.footprintdiary.entries.reddit.RedditComment;
import se.roseabrams.footprintdiary.entries.reddit.RedditPost;
import se.roseabrams.footprintdiary.entries.resfiles.ResFile;
import se.roseabrams.footprintdiary.entries.skype.SkypeMessage;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.entries.steam.SteamStore;
import se.roseabrams.footprintdiary.entries.whatsapp.WhatsAppMessage;
import se.roseabrams.footprintdiary.entries.wikimedia.WikimediaEdit;
import se.roseabrams.footprintdiary.entries.youtube.YouTubePlayback;

public class DiaryWriter {
    private static final String D = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";

    public static void main(String[] args) {
        DiaryBook d = new DiaryBook(new DiaryDate((short) 2010, (byte) 1, (byte) 1),
                new DiaryDate((short) 2023, (byte) 6, (byte) 30));

        try {
            d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Camera Uploads")));
            //d.add(CameraCapture.createFromLog(new File(D + "dir Camera Uploads.csv")));
            d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina")));
            //d.add(CameraCapture.createFromLog(new File(D + "dir Kina.csv")));
            //d.add(ResFile.createFromLog(new File(D + "dir res.csv")));
            d.add(ResFile.createFromFiles());
            d.add(DiscordMessage.createAllFromCsv(new File(D + "discord\\messages.csv")));
            d.add(WhatsAppMessage.createAllFromTxt(new File(D + "whatsapp")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_0.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_1.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(D + "spotify\\endsong_2.json")));
            d.add(SpotifyPlaylisting.createFromJson(new File(D + "spotify\\Playlist1.json")));
            d.add(SteamStore.createFromHtml(new File(D + "steam\\Purchase History.html")));
            d.add(DailyActivity.createDays(new File(D + "apple\\health.xml")));
            d.add(SkypeMessage.createAllFromTxt(new File(D + "skype")));
            d.add(WikimediaEdit.createFromWebsites());
            d.add(RedditPost.createFromCsv(new File(D + "reddit\\posts.csv")));
            d.add(RedditComment.createFromCsv(new File(D + "reddit\\comments.csv")));
            d.add(YouTubePlayback.createFromHtml(new File(D + "google\\youtube watch.html")));
            System.gc();

            String csvSum = d.csvSum(true);
            Util.writeFile(new File("D:\\Dropbox\\Privat\\postGym program"
                    + "\\footprint diary\\outputs\\diarySumTable.csv"), csvSum);
            System.gc();

            String csvIndex = d.csvIndex();
            Util.writeFile(new File("D:\\Dropbox\\Privat\\postGym program"
                    + "\\footprint diary\\outputs\\diaryIndexTable.csv"), csvIndex);
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
