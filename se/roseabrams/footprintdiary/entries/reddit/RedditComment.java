package se.roseabrams.footprintdiary.entries.reddit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryDateTime;
import se.roseabrams.footprintdiary.Util;

public class RedditComment extends RedditSubmission {

    public final String BODY;
    public final String PARENT_POST_ID;
    public final String PARENT_COMMENT_ID; // may be null

    public RedditComment(DiaryDate dd, String id, String subreddit, int gildings, String body, String parentPostId,
            String parentCommentId) {
        super(dd, id, subreddit, gildings);
        BODY = body;
        PARENT_POST_ID = parentPostId.intern();
        PARENT_COMMENT_ID = parentCommentId.intern();
    }

    @Override
    public String getStringSummary() {
        return BODY;
    }

    @SuppressWarnings("unused")
    public static RedditComment[] createFromCsv(File commentsFile) throws IOException {
        ArrayList<RedditComment> output = new ArrayList<>(1000);
        for (String post : Util.readFileLines(commentsFile)) {
            Scanner s = new Scanner(post);
            s.useDelimiter(",");
            String id = s.next();
            String linkFull = s.next();
            String dateS = s.next();
            DiaryDateTime date = new DiaryDateTime(dateS);
            String ip = s.next();
            String subreddit = s.next();
            int gildings = Integer.parseInt(s.next());
            String postLink = s.next();
            String parentId = s.next();
            if (parentId.isBlank())
                parentId = null;
            String body = s.next();
            String media = s.nextLine();

            int postIdIndex = postLink.indexOf("/comments/") + "/comments/".length();
            String postId = postLink.substring(postIdIndex, postLink.indexOf("/", postIdIndex));

            RedditComment r = new RedditComment(date, id, subreddit, gildings, body, postId, parentId);
            output.add(r);
            s.close();
        }

        return output.toArray(new RedditComment[output.size()]);
    }
}
