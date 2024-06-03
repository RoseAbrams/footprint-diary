package se.roseabrams.footprintdiary.entries.reddit;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.interfaces.PlainText;

public abstract class RedditSubmission extends RedditEvent implements PlainText {

    public final String SUBREDDIT;
    public final int GILDINGS;

    public RedditSubmission(DiaryDate dd, String id, String subreddit, int gildings) {
        super(dd, id);
        SUBREDDIT = subreddit;
        GILDINGS = gildings;
    }
}
