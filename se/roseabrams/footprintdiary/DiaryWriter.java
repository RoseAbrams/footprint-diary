package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.entries.banking.BankEvent;
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

    public static void main(String[] args) {
        String I = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";
        String O = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\outputs\\";

        // DiaryDate dd1 = new DiaryDate((short) 2010, (byte) 1, (byte) 1);
        // DiaryDate dd2 = new DiaryDate((short) 2023, (byte) 6, (byte) 30);
        DiaryDate dd1 = new DiaryDate((short) 2021, (byte) 1, (byte) 1);
        DiaryDate dd2 = new DiaryDate((short) 2021, (byte) 12, (byte) 31);

        DiaryBook d = new DiaryBook(dd1, dd2);

        try {
            d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Camera Uploads")));
            d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Bilder")));
            d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Filmer")));
            d.add(ResFile.createFromFiles());
            d.add(DiscordMessage.createAllFromCsv(new File(I + "discord\\messages")));
            d.add(WhatsAppMessage.createAllFromTxt(new File(I + "whatsapp")));
            d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_0.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_1.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_2.json")));
            d.add(SpotifyPlaylisting.createFromJson(new File(I + "spotify\\Playlist1.json")));
            d.add(SteamStore.createFromHtml(new File(I + "steam\\Purchase History.html")));
            d.add(DailyActivity.createDays(new File(I + "apple\\health.xml")));
            d.add(SkypeMessage.createAllFromTxt(new File(I + "skype")));
            d.add(WikimediaEdit.createFromWebsites());
            d.add(RedditPost.createFromCsv(new File(I + "reddit\\posts.csv")));
            d.add(RedditComment.createFromCsv(new File(I + "reddit\\comments.csv")));
            d.add(YouTubePlayback.createFromHtml(new File(I + "google\\youtube watch.html")));
            // yotube comments? can't find them in export
            d.add(BankEvent.createFromCsv(new File(I + "bank\\PERSONKONTO.csv")));
            System.gc();

            String csvSum = d.sumsCsv(true);
            Util.writeFile(new File(O + "diarySumTable.csv"), csvSum);

            String csvIndex = d.indexCsv();
            Util.writeFile(new File(O + "diaryIndexTable.csv"), csvIndex);
            System.gc();

            String[] prose = d.prose();
            StringBuilder rtf = new StringBuilder(100000);
            rtf.append("{\\rtf\\ansi\\deff0\\widoctrl\\ftnbj \\sectd\\linex0\\endnhere \\pard\\plain \\fs30 ");
            for (String page : prose) {
                rtf.append(page.replace("\n", "\\par ")).append("\\page ");
            }
            rtf.append("}");
            Util.writeFile(new File(O + "diaryProse.rtf"), rtf.toString());
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
