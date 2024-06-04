package se.roseabrams.footprintdiary.entries.youtube;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class YouTubeVideo {

    public final String ID;
    public String title;
    public String channelId;
    public String channelName;
    private static final ArrayList<YouTubeVideo> CACHE = new ArrayList<>(10000);

    public YouTubeVideo(String id, String title, String channelId, String channelName) {
        ID = id;
        this.title = title;
        if (channelId == null) {
            this.channelId = channelId;
            this.channelName = channelName;
        } else {
            this.channelId = channelId.intern();
            this.channelName = channelName.intern();
        }
        try {
            getUrl();
        } catch (AssertionError | RuntimeException e) {
            throw new IllegalArgumentException("Arguments would cause invalid URLs.", e);
        }
    }

    public URL getUrl() {
        try {
            return URI.create("https://youtube.com/watch?v=" + ID).toURL();
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof YouTubeVideo && ID.equals(((YouTubeVideo) o).ID);
    }

    @Override
    public String toString() {
        if (title != null)
            if (channelName != null)
                return channelName + " – " + title;
            else
                return title;
        return ID;
    }

    public static YouTubeVideo create(String id, String title, String channelId, String channelName) {
        assert id != null && !id.isBlank() && id.length() == 11;

        for (YouTubeVideo v : CACHE) {
            if (id.equals(v.ID)) {
                if (v.title == null && title != null)
                    v.title = title;
                if (v.channelId == null && channelId != null)
                    v.channelId = channelId;
                if (v.channelName == null && channelName != null)
                    v.channelName = channelName;
                return v;
            }
        }

        YouTubeVideo t = new YouTubeVideo(id, title, channelId, channelName);
        CACHE.add(t);
        return t;
    }
}
