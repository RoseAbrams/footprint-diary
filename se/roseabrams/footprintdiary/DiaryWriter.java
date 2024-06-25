package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

    private static final String I = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";
    private static final String O = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\outputs\\";
    private static final DiaryDate DD1 = new DiaryDate((short) 2010, (byte) 1, (byte) 1);
    private static final DiaryDate DD2 = new DiaryDate((short) 2023, (byte) 6, (byte) 30);
    //private static final DiaryDate DD1 = new DiaryDate((short) 2021, (byte) 1, (byte) 1);
    //private static final DiaryDate DD2 = new DiaryDate((short) 2021, (byte) 12, (byte) 31);

    public static void main(String[] args) {

        final DiaryBook D = new DiaryBook(DD1, DD2);

        try {/*
             d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Camera Uploads")));
             d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Bilder")));
             d.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Filmer")));
             d.add(ResFile.createFromFiles());
             d.add(DiscordMessage.createAllFromCsv(new File(I + "discord\\messages")));
             d.add(WhatsAppMessage.createAllFromFolder(new File(I + "whatsapp")));
             d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_0.json")));
             d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_1.json")));
             d.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_2.json")));
             d.add(SpotifyPlaylisting.createFromJson(new File(I + "spotify\\Playlist1.json")));
             */
            // debugged up to here
            D.add(SteamStoreEvent.createFromHtml(new File(I + "steam\\Purchase History.html")));
            D.add(DailyActivity.createDays(new File(I + "apple\\health.xml")));
            D.add(SkypeMessage.createAllFromTxt(new File(I + "skype")));
            D.add(WikimediaEdit.createFromWebsites());
            D.add(RedditPost.createFromCsv(new File(I + "reddit\\posts.csv")));
            D.add(RedditComment.createFromCsv(new File(I + "reddit\\comments.csv")));
            D.add(YouTubePlayback.createFromHtml(new File(I + "google\\youtube watch.html")));
            //D.add(YouTubeComment.createFromCsv(new File(I + "google\\youtube comments.csv")));
            //D.add(YouTubeComment.createFromCsv(new File(I + "google\\youtube old comments.csv")));
            //D.add(youtubeCommentHack());
            D.add(BankEvent.createFromCsv(new File(I + "bank\\PERSONKONTO.csv")));
            D.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem.ics")));
            D.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem1.ics")));
            D.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem2.ics")));
            D.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem3.ics")));
            D.add(FacebookPost.createFromHtml(new File(I + "your_posts__check_ins__photos_and_videos_1.html")));
            D.add(FacebookPost.createFromHtml(new File(I + "group_posts_and_comments.html")));
            D.add(FacebookComment.createFromHtml(new File(I + "comments.html")));
            D.add(FacebookComment.createFromHtml(new File(I + "your_comments_in_groups.html")));
            D.add(FacebookReaction.createFromHtml(new File(I + "likes_and_reactions_1.html")));
            D.add(FacebookReaction.createFromHtml(new File(I + "likes_and_reactions_2.html")));
            D.add(FacebookReaction.createFromHtml(new File(I + "likes_and_reactions_3.html")));
            D.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\inbox")));
            D.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\archived_threads")));
            D.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\filtered_threads")));
            System.gc();

            String csvSum = D.sumsCsv(true);
            Util.writeFile(new File(O + "diarySumTable.csv"), csvSum);

            String csvIndex = D.indexCsv();
            Util.writeFile(new File(O + "diaryIndexTable.csv"), csvIndex);
            System.gc();

            String[] prose = D.prose();
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

    private static ArrayList<YouTubeComment> youtubeCommentHack() throws IOException {
        ArrayList<YouTubeComment> unsorted = new ArrayList<>();
        ArrayList<YouTubeComment> sorted = new ArrayList<>();
        unsorted.addAll(Arrays.asList(YouTubeComment.createFromHtml(new File(I + "google\\youtube comments.html"))));
        unsorted.addAll(Arrays.asList(YouTubeComment.createFromHtml(new File(I + "google\\youtube old comments.html"))));
        unsorted.addAll(Arrays.asList(YouTubeComment.createFromCsv(new File(I + "google\\youtube comments.csv"))));
        unsorted.addAll(Arrays.asList(YouTubeComment.createFromCsv(new File(I + "google\\youtube old comments.csv"))));
        for (YouTubeComment c1 : unsorted) {
            YouTubeComment better = null;
            for (YouTubeComment c2 : unsorted) {
                if (c1 != c2 && c1.equals(c2)) {
                    if ((c1.VIDEO == null && c2.VIDEO != null) || (c1.textTruncated() && !c2.textTruncated())) {
                        better = c2;
                        continue;
                    }
                }
            }
            // uuggghhh it don't solve duplicate adding into the result
            if (better == null) {
                sorted.add(c1);
            } else {
                sorted.add(better);
            }
        }
        return sorted;
    }
}
