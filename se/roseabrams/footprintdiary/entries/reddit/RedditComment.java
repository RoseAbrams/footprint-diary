package se.roseabrams.footprintdiary.entries.reddit;

import se.roseabrams.footprintdiary.DiaryDate;

public class RedditComment extends RedditSubmission {

    public final String BODY;
    public final String PARENT_POST_ID;
    public final String PARENT_COMMENT_ID; // may be null

    public RedditComment(DiaryDate dd, String id, String subreddit, int gildings, String body, String parentPostId,
            String parentCommentId) {
        super(dd, id, subreddit, gildings);
        BODY = body;
        PARENT_POST_ID = parentPostId;
        PARENT_COMMENT_ID = parentCommentId;
    }

    @Override
    public String getStringSummary() {
        return BODY;
    }
}
