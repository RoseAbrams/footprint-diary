package se.roseabrams.footprintdiary.entries.reddit;

import java.net.URL;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;

public class RedditMediaPost extends RedditPost implements RemoteResource {

    public final URL MEDIA;

    public RedditMediaPost(DiaryDate dd, String id, String subreddit, int gildings, String title, String body, URL media) {
        super(dd, id, subreddit, gildings, title, body);
        MEDIA = media;
    }

    @Override
    public String getPathToResource() {
        return MEDIA.getPath();
    }

    @Override
    public URL getUrlOfResource() {
        return MEDIA;
    }
/*
    @Override
    public StringBuilder detailedCsv(StringBuilder s, String delim) {
        return super.detailedCsv(s, delim).append(delim).append(MEDIA);
    }*/
}
