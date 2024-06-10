package se.roseabrams.footprintdiary.entries.reddit;

import se.roseabrams.footprintdiary.DiaryDate;

public abstract class RedditSubmission extends RedditEvent {

    public final String SUBREDDIT;
    public final int GILDINGS;

    public RedditSubmission(DiaryDate dd, String id, String subreddit, int gildings) {
        super(dd, id);
        SUBREDDIT = subreddit.intern();
        GILDINGS = gildings;
    }
}
