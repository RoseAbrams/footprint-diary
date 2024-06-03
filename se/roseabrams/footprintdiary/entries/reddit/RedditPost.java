package se.roseabrams.footprintdiary.entries.reddit;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.Util;
import se.roseabrams.footprintdiary.interfaces.RichText;

public class RedditPost extends RedditSubmission implements RichText {

    public final String TITLE;
    public final String BODY;

    public RedditPost(DiaryDate dd, String id, String subreddit, int gildings, String title, String body) {
        super(dd, id, subreddit, gildings);
        TITLE = title;
        BODY = body;
    }

    @Override
    public String getStringSummary() {
        return TITLE;
    }

    public static RedditPost[] createFromCsv(File postsFile) throws IOException {
        ArrayList<RedditPost> output = new ArrayList<>(1000);
        for (String post : Util.readFileLines(postsFile)) {
            Scanner s = new Scanner(post);
            s.useDelimiter(",");
            String id = s.next();
            String linkFull = s.next();
            String dateS = s.next();
            DiaryDateTime date = new DiaryDateTime(dateS);
            String ip = s.next();
            String subreddit = s.next();
            int gildings = Integer.parseInt(s.next());
            String title = s.next();
            String mediaS = s.next();
            if (mediaS.startsWith("/r/")) {
                mediaS = null; // sometimes it points to itself for text posts
            }
            String body = s.nextLine();

            URL media = null;
            if (mediaS != null) {
                try {
                    media = URI.create(mediaS).toURL();
                } catch (MalformedURLException e) {
                    s.close();
                    throw new IllegalArgumentException("Invalid URL given as media attachment: " + mediaS, e);
                }
            }

            RedditPost r;
            if (media == null)
                r = new RedditPost(date, id, subreddit, gildings, title, body);
            else
                r = new RedditMediaPost(date, id, subreddit, gildings, title, body, media);
            output.add(r);
            s.close();
        }

        return output.toArray(new RedditPost[output.size()]);
    }
}
