package se.roseabrams.footprintdiary.entries.reddit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.roseabrams.footprintdiary.CSVParser;
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
    public static List<RedditPost> createFromCsv(File postsFile) throws IOException {
        ArrayList<RedditPost> output = new ArrayList<>(1000);
        String posts = Util.readFile(postsFile);
        CSVParser s = new CSVParser(posts, ",", "\r\n");
        s.nextLine(); // skip past headers
        while (s.hasNext()) {
            String id = s.next();
            String linkFull = s.next();
            String dateS = s.next();
            DiaryDateTime date = new DiaryDateTime(dateS);
            String ip = s.next(); // almost always empty, probably a recently added field
            String subreddit = s.next();
            int gildings = Integer.parseInt(s.next());
            String title = s.next();
            String mediaS = s.next();
            if (mediaS.contains("/r/")) {
                String linkedSubreddit = mediaS.substring(mediaS.indexOf("/r/") + 3, mediaS.indexOf("/comments"));
                if (subreddit.equals(linkedSubreddit))
                    mediaS = ""; // sometimes text posts links to itself
            }
            String body = s.nextLine();

            RedditPost r;
            if (!mediaS.isBlank())
                r = new RedditMediaPost(date, id, subreddit, gildings, title, body, mediaS);
            else
                r = new RedditPost(date, id, subreddit, gildings, title, body);
            output.add(r);
        }
        s.close();

        return output;
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
