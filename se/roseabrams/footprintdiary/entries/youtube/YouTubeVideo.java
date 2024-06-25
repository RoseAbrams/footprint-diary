package se.roseabrams.footprintdiary.entries.youtube;

import java.util.ArrayList;

import se.roseabrams.footprintdiary.common.ContentType;
import se.roseabrams.footprintdiary.common.Webpage;

public class YouTubeVideo extends Webpage {

    public final String ID;
    private String title;
    private String channelId;
    private String channelName;
    private static final ArrayList<YouTubeVideo> CACHE = new ArrayList<>(10000);

    private YouTubeVideo(String id, String title, String channelId, String channelName) {
        super("https://youtube.com/watch?v=" + id, ContentType.VIDEO);
        ID = id;
        this.title = title;
        if (channelId == null) {
            this.channelId = channelId;
            this.channelName = channelName;
        } else {
            this.channelId = channelId.intern();
            this.channelName = channelName.intern();
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof YouTubeVideo y2 && ID.equals(y2.ID);
    }

    @Override
    public String toString() {
        if (title != null)
            if (channelName != null)
                return channelName + " – " + title;
            else
                return title;
        else
            return ID;
    }

    public static YouTubeVideo getOrCreate(String id, String title, String channelId, String channelName) {
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
