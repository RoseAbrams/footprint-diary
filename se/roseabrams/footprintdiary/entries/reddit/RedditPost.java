package se.roseabrams.footprintdiary.entries.reddit;

import se.roseabrams.footprintdiary.DiaryDate;
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
}
