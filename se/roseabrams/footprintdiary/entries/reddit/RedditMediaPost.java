package se.roseabrams.footprintdiary.entries.reddit;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;
import se.roseabrams.footprintdiary.common.RemoteContent;

public class RedditMediaPost extends RedditPost implements ContentContainer {

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
