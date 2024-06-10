package se.roseabrams.footprintdiary.entries.reddit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.Util;

public class RedditPost extends RedditSubmission {

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

    @SuppressWarnings("unused")
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
                mediaS = ""; // sometimes it points to itself for text posts
            }
            String body = s.nextLine();

            RedditPost r;
            if (!mediaS.isBlank())
                r = new RedditMediaPost(date, id, subreddit, gildings, title, body, mediaS);
            else
                r = new RedditPost(date, id, subreddit, gildings, title, body);
            output.add(r);
            s.close();
        }

        return output.toArray(new RedditPost[output.size()]);
    }
    /*
     * @Override
     * public StringBuilder detailedCsv(StringBuilder s, String delim) {
     * return
     * s.append(ID).append(TITLE).append(delim).append(BODY).append(delim).append(
     * SUBREDDIT).append(delim).append(GILDINGS);
     * }
     */
}
