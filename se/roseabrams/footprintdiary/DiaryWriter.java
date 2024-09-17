package se.roseabrams.footprintdiary;

import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

import se.roseabrams.footprintdiary.entries.apple.CalendarEvent;
import se.roseabrams.footprintdiary.entries.apple.DailyActivity;
import se.roseabrams.footprintdiary.entries.banking.BankEvent;
import se.roseabrams.footprintdiary.entries.camera.CameraCapture;
import se.roseabrams.footprintdiary.entries.discord.DiscordMessage;
import se.roseabrams.footprintdiary.entries.email.Email;
import se.roseabrams.footprintdiary.entries.facebook.FacebookComment;
import se.roseabrams.footprintdiary.entries.facebook.FacebookFriend;
import se.roseabrams.footprintdiary.entries.facebook.FacebookMessage;
import se.roseabrams.footprintdiary.entries.facebook.FacebookPost;
import se.roseabrams.footprintdiary.entries.facebook.FacebookReaction;
import se.roseabrams.footprintdiary.entries.medical.MedicalRecord;
import se.roseabrams.footprintdiary.entries.reddit.RedditComment;
import se.roseabrams.footprintdiary.entries.reddit.RedditPost;
import se.roseabrams.footprintdiary.entries.resfiles.ResFile;
import se.roseabrams.footprintdiary.entries.skype.SkypeMessage;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlayback;
import se.roseabrams.footprintdiary.entries.spotify.SpotifyPlaylisting;
import se.roseabrams.footprintdiary.entries.steam.SteamLicenseEvent;
import se.roseabrams.footprintdiary.entries.steam.SteamStoreEvent;
import se.roseabrams.footprintdiary.entries.steam2.SteamAchievment;
import se.roseabrams.footprintdiary.entries.steam2.SteamFetcher;
import se.roseabrams.footprintdiary.entries.tinder.TinderSwipe;
import se.roseabrams.footprintdiary.entries.twitch.TwitchChatMessage;
import se.roseabrams.footprintdiary.entries.twitch.TwitchFollow;
import se.roseabrams.footprintdiary.entries.twitch.TwitchSession;
import se.roseabrams.footprintdiary.entries.whatsapp.WhatsAppMessage;
import se.roseabrams.footprintdiary.entries.wikimedia.WikimediaEdit;
import se.roseabrams.footprintdiary.entries.youtube.YouTubeComment;
import se.roseabrams.footprintdiary.entries.youtube.YouTubeEvent;

public class DiaryWriter {

    private final DiaryBook D = new DiaryBook();
    private static final String I = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\data\\";
    private static final String O = "D:\\Dropbox\\Privat\\postGym program\\footprint diary\\outputs\\";

    public static void main(String[] args) {
        System.out.println("DiaryWriter started");
        try {
            File diarySer = new File(O + "diary.ser");
            DiaryWriter DW = null;

            if (diarySer.exists()) {
                Object dwO = Util.deserializeOrDelete(diarySer);
                if (dwO != null)
                    DW = (DiaryWriter) dwO;
            }

            if (DW == null) {
                DW = new DiaryWriter();

                for (DiaryIngestCategory c : DiaryIngestCategory.values()) {
                    if (c.enabled())
                        DW.add(ingest(c), c);
                }

                DW.D.trimPagesAfter(DiaryDate.TODAY.yesterday());
                DW.D.addFillerPages();
                Util.serialize(DW.D, diarySer);
            }

            DW.writeDataBounds(new File(O + "diaryDataBounds.csv"));
            DW.writeCsvSum(new File(O + "diarySumTable.csv"));
            DW.writeCsvIndex(new File(O + "diaryIndexTable.csv"));
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
        System.out.println("DiaryWriter exited normally");
    }

    private static List<DiaryEntry> ingest(DiaryIngestCategory c) throws IOException {
        File categorySer = new File(O + c.serializationFilename());
        if (categorySer.exists()) {
            Object deserO = Util.deserializeOrDelete(categorySer);
            if (deserO != null) {
                List<DiaryEntry> deserL = (List<DiaryEntry>) deserO;
                System.out.println("deserialized " + deserL.size() + " ingested entries from " + c);
                return deserL;
            }
        }

        ArrayList<DiaryEntry> output = new ArrayList<>();
        switch (c) {
            case CAMERA:
                output.addAll(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Camera Uploads")));
                output.addAll(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Bilder")));
                output.addAll(CameraCapture.createFromFiles(new File("D:\\Dropbox\\Privat\\utdaterat\\Kina\\Filmer")));
                break;
            case RES:
                output.addAll(ResFile.createFromFiles());
                break;
            case DISCORD:
                output.addAll(DiscordMessage.createAllFromCsv(new File(I + "discord\\messages")));
                break;
            case WHATSAPP:
                output.addAll(WhatsAppMessage.createAllFromFolder(new File(I + "whatsapp")));
                break;
            case SPOTIFY:
                output.addAll(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_0.json")));
                output.addAll(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_1.json")));
                output.addAll(SpotifyPlayback.createAllFromJson(new File(I + "spotify\\endsong_2.json")));
                output.addAll(SpotifyPlaylisting.createFromJson(new File(I + "spotify\\Playlist1.json")));
                break;
            case STEAM:
                output.addAll(SteamStoreEvent.createFromHtml(new File(I + "steam\\Purchase History.html")));
                output.addAll(SteamLicenseEvent.createFromHtml(new File(I + "steam\\Licenses.html")));
                SteamFetcher steamApi = new SteamFetcher();
                output.addAll(SteamAchievment.createFromApi(steamApi));
                break;
            case APPLE_ACTIVITY:
                output.addAll(DailyActivity.createDays(new File(I + "apple\\health export.xml")));
                break;
            case SKYPE:
                output.addAll(SkypeMessage.createAllFromTxt(new File(I + "skype")));
                break;
            case WIKIMEDIA:
                output.addAll(WikimediaEdit.createFromWebsites());
                break;
            case REDDIT:
                output.addAll(RedditPost.createFromCsv(new File(I + "reddit\\posts.csv")));
                output.addAll(RedditComment.createFromCsv(new File(I + "reddit\\comments.csv")));
                // messages?
                break;
            case YOUTUBE:
                output.addAll(YouTubeEvent.createHistoryFromHtml(new File(I + "google\\youtube old watch.html")));
                output.addAll(YouTubeEvent.createHistoryFromHtml(new File(I + "google\\youtube watch.html")));
                output.addAll(YouTubeEvent.createHistoryFromHtml(new File(I + "google\\youtube search and ads.html")));
                output.addAll(
                        YouTubeEvent.createHistoryFromHtml(new File(I + "google\\youtube old search and ads.html")));
                output.addAll(YouTubeComment.createFromHtml(new File(I + "google\\youtube comments.html")));
                output.addAll(YouTubeComment.createFromHtml(new File(I + "google\\youtube old comments.html")));
                /*output.addAll(YouTubeComment.createFromCsv(new File(I + "google\\youtube comments.csv")));
                output.addAll(YouTubeComment.createFromCsv(new File(I + "google\\youtube old comments.csv")));
                output.addAll(youtubeCommentHack());*/
                break;
            case BANKING:
                output.addAll(BankEvent.createFromCsv(new File(I + "bank\\PERSONKONTO.csv")));
                output.addAll(BankEvent.createFromAzureJson(new File(I + "bank\\pdfs to jsons")));
                break;
            case APPLE_CALENDAR:
                output.addAll(CalendarEvent.createFromIcs(new File(I + "apple\\Hem.ics")));
                output.addAll(CalendarEvent.createFromIcs(new File(I + "apple\\Hem1.ics")));
                output.addAll(CalendarEvent.createFromIcs(new File(I + "apple\\Hem2.ics")));
                output.addAll(CalendarEvent.createFromIcs(new File(I + "apple\\Hem3.ics")));
                break;
            case FACEBOOK:
                output.addAll(FacebookPost.createFromHtml(
                        new File(I + "facebook\\your_posts__check_ins__photos_and_videos_1.html")));
                //output.addAll(FacebookPost.createFromHtml(new File(I + "facebook\\group_posts_and_comments.html"))); // not working, is it worth fixing?
                output.addAll(FacebookComment.createFromHtml(new File(I + "facebook\\comments.html")));
                output.addAll(FacebookComment.createFromHtml(new File(I + "facebook\\your_comments_in_groups.html")));
                output.addAll(FacebookReaction.createFromHtml(new File(I + "facebook\\likes_and_reactions_1.html")));
                output.addAll(FacebookReaction.createFromHtml(new File(I + "facebook\\likes_and_reactions_2.html")));
                output.addAll(FacebookReaction.createFromHtml(new File(I + "facebook\\likes_and_reactions_3.html")));
                // "your_photos.html", "your_videos.html", "your_uncategorized_photos.html"
                // "profile_update_history.html", "pages_you've_liked.html"
                // "connected_apps_and_websites.html", "recently_viewed.html", "who_you've_followed.html"
                output.addAll(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\inbox")));
                output.addAll(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\archived_threads")));
                output.addAll(FacebookMessage.createFromFolder(new File(I + "facebook\\messages\\filtered_threads")));
                output.addAll(FacebookFriend.createFromFolder(new File(I + "facebook\\friends")));
                break;
            case MEDICAL:
                output.addAll(MedicalRecord.createFromHtml(new File(I + "1177.html")));
                break;
            case TINDER:
                output.addAll(TinderSwipe.createFromJson(
                        new File("D:\\Dropbox\\Privat\\postGym program\\gasoline\\final data\\tinder dataset.json")));
                break;
            case EMAIL_GMAIL: // untested
                output.addAll(Email.createFromMbox(new File(I + "google\\All mail Including Spam and Trash.mbox")));
                break;
            case EMAIL_HOTMAIL: // untested
                output.addAll(Email.createFromPst(
                        new File(I + "outlook\\outlook backup 2023-05-04 - 8fbe75217ef14aa3a4fdfc010ace07f9.pst")));
                break;
            case TWITCH: // untested
                output.addAll(TwitchChatMessage.createFromCsv(new File(I + "twitch\\site_history\\chat_messages.csv")));
                output.addAll(TwitchFollow.createFromCsv(
                        new File(I + "twitch\\community\\follows\\follow.csv"),
                        new File(I + "twitch\\community\\follows\\unfollow.csv")));
                output.addAll(TwitchSession.createSessions(new File(I + "twitch\\site_history\\minute_watched.csv")));
                break;
            default:
                System.err.println("DiaryIngestCategory not implemented:" + c);
                break;
        }

        Util.serialize(output, categorySer);
        System.out.println("ingested and saved " + output.size() + " entries from " + c);
        System.gc();
        return output;
    }

    public DiaryWriter() {
    }

    public void add(List<DiaryEntry> de, DiaryIngestCategory c) {
        D.add(de);
        D.add(DiaryDataBoundary.createForList(de, c));
    }

    public void writeDataBounds(File outputFile) throws IOException {
        ArrayList<DiaryEntry> bounds = D.filter(e -> e.CATEGORY == DiaryEntryCategory.DATA_BOUNDARY);
        StringBuilder csvBounds = new StringBuilder(1000);
        for (DiaryIngestCategory c : DiaryIngestCategory.values()) {
            csvBounds.append(c).append(",");
            for (DiaryEntry e : bounds) {
                if (e instanceof DiaryDataBoundary eb && eb.INGEST_CATEGORY == c)
                    csvBounds.append(e.DATE).append(","); // start and finish will sort itself from chronology
            }
            csvBounds.append("\n");
        }
        Util.writeFile(outputFile, csvBounds.toString());
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
        unsorted.addAll(YouTubeComment.createFromHtml(new File(I + "google\\youtube comments.html")));
        unsorted.addAll(YouTubeComment.createFromHtml(new File(I + "google\\youtube old comments.html")));
        unsorted.addAll(YouTubeComment.createFromCsv(new File(I + "google\\youtube comments.csv")));
        unsorted.addAll(YouTubeComment.createFromCsv(new File(I + "google\\youtube old comments.csv")));
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
