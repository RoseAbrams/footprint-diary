package se.roseabrams.footprintdiary.entries.reddit;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.content.Content;
import se.roseabrams.footprintdiary.content.RemoteContent;
import se.roseabrams.footprintdiary.interfaces.ContentOwner;

public class RedditMediaPost extends RedditPost implements ContentOwner {

    public final RemoteContent MEDIA;

    public RedditMediaPost(DiaryDate dd, String id, String subreddit, int gildings, String title, String body,
            String mediaS) {
        super(dd, id, subreddit, gildings, title, body);
        MEDIA = new RemoteContent(mediaS);
    }

    @Override
    public Content getContent() {
        return MEDIA;
    }
    /*
     * @Override
     * public StringBuilder detailedCsv(StringBuilder s, String delim) {
     * return super.detailedCsv(s, delim).append(delim).append(MEDIA);
     * }
     */
}
