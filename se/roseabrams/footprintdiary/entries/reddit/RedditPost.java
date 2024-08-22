package se.roseabrams.footprintdiary.entries.reddit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    /// TODO needs multiline support
    public static RedditPost[] createFromCsv(File postsFile) throws IOException {
        ArrayList<RedditPost> output = new ArrayList<>(1000);
        List<String> postsLines = Util.readFileLines(postsFile);
        for (int i = 1; i < postsLines.size(); i++) {
            Scanner s = new Scanner(postsLines.get(i));
            s.useDelimiter(",");
            String id = s.next();
            String linkFull = s.next();
            String dateS = s.next();
            DiaryDateTime date = new DiaryDateTime(dateS);
            String ip = s.next();
            assert ip.isEmpty(); // not sure why this exists or is supposed to mean
            String subreddit = s.next();
            int gildings = Integer.parseInt(s.next());
            String title = s.next(); // TODO handle internal commas
            if (title.charAt(0) == '\"')
                title = title.substring(1, title.length() - 1);
            title = title.replace("\"\"", "\"");
            String mediaS = s.next();
            if (mediaS.startsWith("/r/")) // sometimes it points to itself for text posts // TODO check behavior when crossposting
                mediaS = "";
            String body = s.nextLine(); // TODO handle internal commas
            body = body.substring(1); // remove opening comma
            while (i + 1 < postsLines.size() && postsLines.get(i + 1).length() < 100
                    || (!postsLines.get(i + 1).subSequence(6, 11).equals(",http")
                            && !postsLines.get(i + 1).subSequence(7, 12).equals(",http"))) {
                i++;
                body += "\n" + postsLines.get(i);
            }
            if (!body.isEmpty() && body.charAt(0) == '\"')
                body = body.substring(1, body.length() - 1);
            body = body.replace("\"\"", "\"");
            body = body.replace("\"*", "*");

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
