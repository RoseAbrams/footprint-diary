package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import se.roseabrams.footprintdiary.entries.apple.CalendarEvent;
import se.roseabrams.footprintdiary.entries.apple.DailyActivity;
import se.roseabrams.footprintdiary.entries.banking.BankEvent;
import se.roseabrams.footprintdiary.entries.camera.CameraCapture;
import se.roseabrams.footprintdiary.entries.discord.DiscordMessage;
import se.roseabrams.footprintdiary.entries.facebook.FacebookComment;
import se.roseabrams.footprintdiary.entries.facebook.FacebookFriend;
import se.roseabrams.footprintdiary.entries.facebook.FacebookMessage;
import se.roseabrams.footprintdiary.entries.facebook.FacebookPost;
import se.roseabrams.footprintdiary.entries.facebook.FacebookReaction;
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
import se.roseabrams.footprintdiary.entries.youtube.YouTubeEvent;

public class DiaryWriter {

    private final DiaryBook D;
    private static final String I = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";
    private static final String O = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\outputs\\";

    public static void main(String[] args) {

        final DiaryWriter DW = new DiaryWriter();

        try {
            DW.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Camera Uploads")));
            DW.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Bilder")));
            DW.add(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Filmer")));
            DW.add(ResFile.createFromFiles());
            DW.add(DiscordMessage.createAllFromCsv(new File(I + "discord\\messages")));
            DW.add(WhatsAppMessage.createAllFromFolder(new File(I + "whatsapp")));
            DW.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_0.json")));
            DW.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_1.json")));
            DW.add(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_2.json")));
            DW.add(SpotifyPlaylisting.createFromJson(new File(I + "spotify\\Playlist1.json")));
            DW.add(SteamStoreEvent.createFromHtml(new File(I + "steam\\Purchase History.html")));//has problems
            DW.add(DailyActivity.createDays(new File(I + "apple\\health export.xml")));
            DW.add(SkypeMessage.createAllFromTxt(new File(I + "skype")));
            DW.add(WikimediaEdit.createFromWebsites());//has problems
            DW.add(RedditPost.createFromCsv(new File(I + "reddit\\posts.csv")));//has problems
            DW.add(RedditComment.createFromCsv(new File(I + "reddit\\comments.csv")));//has problems
            DW.add(YouTubeEvent.createFromHtml(new File(I + "google\\youtube old watch.html")));
            DW.add(YouTubeEvent.createFromHtml(new File(I + "google\\youtube watch.html")));
            DW.add(YouTubeEvent.createFromHtml(new File(I + "google\\youtube search and ads.html")));
            DW.add(YouTubeEvent.createFromHtml(new File(I + "google\\youtube old search and ads.html")));
            DW.add(YouTubeComment.createFromHtml(new File(I + "google\\youtube comments.html")));
            DW.add(YouTubeComment.createFromHtml(new File(I + "google\\youtube old comments.html")));
            //D.add(YouTubeComment.createFromCsv(new File(I + "google\\youtube comments.csv")));
            //D.add(YouTubeComment.createFromCsv(new File(I + "google\\youtube old comments.csv")));
            //D.add(youtubeCommentHack());
            // debugged up to here
            DW.add(BankEvent.createFromCsv(new File(I + "bank\\PERSONKONTO.csv")));
            DW.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem.ics")));
            DW.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem1.ics")));
            DW.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem2.ics")));
            DW.add(CalendarEvent.createFromIcs(new File(I + "apple\\Hem3.ics")));
            DW.add(FacebookPost
                    .createFromHtml(new File(I + "facebook\\your_posts__check_ins__photos_and_videos_1.html")));
            DW.add(FacebookPost.createFromHtml(new File(I + "facebook\\group_posts_and_comments.html")));
            DW.add(FacebookComment.createFromHtml(new File(I + "facebook\\comments.html")));
            DW.add(FacebookComment.createFromHtml(new File(I + "facebook\\your_comments_in_groups.html")));
            DW.add(FacebookReaction.createFromHtml(new File(I + "facebook\\likes_and_reactions_1.html")));
            DW.add(FacebookReaction.createFromHtml(new File(I + "facebook\\likes_and_reactions_2.html")));
            DW.add(FacebookReaction.createFromHtml(new File(I + "facebook\\likes_and_reactions_3.html")));
            DW.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\inbox")));
            DW.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\archived_threads")));
            DW.add(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\filtered_threads")));
            DW.add(FacebookFriend.createFromFolder(new File(I + "facebook\\friends")));
            System.gc();

            Util.serialize(DW.D, new File(O + "diary.ser"));

            DW.D.addFillerPages();

            DW.writeCsvSum(new File(O + "diarySumTable.csv"));
            DW.writeCsvIndex(new File(O + "diaryIndexTable.csv"));
            System.gc();

            DW.writeProseSummary(new File(O + "diaryProse.rtf"));
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

    public DiaryWriter() {
        D = new DiaryBook();
    }

    public void add(DiaryEntry[] de) {
        D.add(de);
        //DiaryEntrySpanBoundary[] desb = DiaryEntrySpanBoundary.create(de, description);
    }

    public void writeCsvSum(File outputFile) throws IOException {
        String csvSum = D.sumsCsv(true);
        Util.writeFile(outputFile, csvSum);
    }

    public void writeCsvIndex(File outputFile) throws IOException {
        String csvIndex = D.indexCsv();
        Util.writeFile(outputFile, csvIndex);
    }

    public void writeProseSummary(File outputFile) throws IOException {
        String[] prose = D.prose();
        StringBuilder rtf = new StringBuilder(100000);
        rtf.append("{\\rtf\\ansi\\deff0\\widoctrl\\ftnbj \\sectd\\linex0\\endnhere \\pard\\plain \\fs30 ");
        for (String page : prose) {
            rtf.append(page.replace("\n", "\\par ")).append("\\page ");
        }
        rtf.append("}");
        Util.writeFile(outputFile, rtf.toString());
    }

    private static ArrayList<YouTubeComment> youtubeCommentHack() throws IOException {
        ArrayList<YouTubeComment> unsorted = new ArrayList<>();
        ArrayList<YouTubeComment> sorted = new ArrayList<>();
        unsorted.addAll(Arrays.asList(YouTubeComment.createFromHtml(new File(I + "google\\youtube comments.html"))));
        unsorted.addAll(
                Arrays.asList(YouTubeComment.createFromHtml(new File(I + "google\\youtube old comments.html"))));
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
    /*  future datasources:
     *      facebook
     *          connected_apps_and_websites.html
     *          recently_viewed.html
     *          who_you've_followed.html
     */
}
