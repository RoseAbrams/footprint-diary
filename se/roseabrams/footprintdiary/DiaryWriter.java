package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;

import se.roseabrams.footprintdiary.entries.apple.CalendarEvent;
import se.roseabrams.footprintdiary.entries.banking.BankEvent;
import se.roseabrams.footprintdiary.entries.camera.CameraCapture;
import se.roseabrams.footprintdiary.entries.discord.DiscordMessage;
import se.roseabrams.footprintdiary.entries.facebook.FacebookComment;
import se.roseabrams.footprintdiary.entries.facebook.FacebookMessage;
import se.roseabrams.footprintdiary.entries.facebook.FacebookPost;
import se.roseabrams.footprintdiary.entries.facebook.FacebookReaction;
import se.roseabrams.footprintdiary.entries.health.DailyActivity;
import se.roseabrams.footprintdiary.entries.reddit.RedditComment;
import se.roseabrams.footprintdiary.entries.reddit.RedditPost;
import se.roseabrams.footprintdiary.entries.resfiles.ResFile;
import se.roseabrams.footprintdiary.entries.skype.SkypeMessage;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.entries.steam.SteamStoreEvent;
import se.roseabrams.footprintdiary.entries.whatsapp.WhatsAppMessage;
import se.roseabrams.footprintdiary.entries.wikimedia.WikimediaEdit;
import se.roseabrams.footprintdiary.entries.youtube.YouTubeComment;
import se.roseabrams.footprintdiary.entries.youtube.YouTubePlayback;

public class DiaryWriter {

    public static void main(String[] args) {
        final String I = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";
        final String O = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\outputs\\";

        final DiaryDate dd1 = new DiaryDate((short) 2010, (byte) 1, (byte) 1);
        final DiaryDate dd2 = new DiaryDate((short) 2023, (byte) 6, (byte) 30);
        //final DiaryDate dd1 = new DiaryDate((short) 2021, (byte) 1, (byte) 1);
        //final DiaryDate dd2 = new DiaryDate((short) 2021, (byte) 12, (byte) 31);

        final DiaryBook d = new DiaryBook(dd1, dd2);

        try {
            d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Camera Uploads")));
            d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Bilder")));
            d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Filmer")));
            d.add(ResFile.createFromFiles());
            d.add(DiscordMessage.createAllFromCsv(new File(I + "discord\\messages")));
            // debugged up to here
            d.add(WhatsAppMessage.createAllFromFolder(new File(I + "whatsapp")));
            d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_0.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_1.json")));
            d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_2.json")));
            d.add(SpotifyPlaylisting.createFromJson(new File(I + "spotify\\Playlist1.json")));
            d.add(SteamStoreEvent.createFromHtml(new File(I + "steam\\Purchase History.html")));
            d.add(DailyActivity.createDays(new File(I + "apple\\health.xml")));
            d.add(SkypeMessage.createAllFromTxt(new File(I + "skype")));
            d.add(WikimediaEdit.createFromWebsites());
            d.add(RedditPost.createFromCsv(new File(I + "reddit\\posts.csv")));
            d.add(RedditComment.createFromCsv(new File(I + "reddit\\comments.csv")));
            d.add(YouTubePlayback.createFromHtml(new File(I + "google\\youtube watch.html")));
            //d.add(YouTubeComment.createFromHtml());
            //d.add(YouTubeComment.createFromCsv());
            d.add(BankEvent.createFromCsv(new File(I + "bank\\PERSONKONTO.csv")));
            d.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem.ics")));
            d.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem1.ics")));
            d.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem2.ics")));
            d.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem3.ics")));
            d.add(FacebookPost.createFromHtml(new File(I + "your_posts__check_ins__photos_and_videos_1.html")));
            d.add(FacebookPost.createFromHtml(new File(I + "group_posts_and_comments.html")));
            d.add(FacebookComment.createFromHtml(new File(I + "comments.html")));
            d.add(FacebookComment.createFromHtml(new File(I + "your_comments_in_groups.html")));
            d.add(FacebookReaction.createFromHtml(new File(I + "likes_and_reactions_1.html")));
            d.add(FacebookReaction.createFromHtml(new File(I + "likes_and_reactions_2.html")));
            d.add(FacebookReaction.createFromHtml(new File(I + "likes_and_reactions_3.html")));
            d.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\inbox")));
            d.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\archived_threads")));
            d.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\filtered_threads")));
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
