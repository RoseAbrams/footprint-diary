package se.roseabrams.footprintdiary.entries.youtube;

import java.net.URL;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.interfaces.RemoteResource;
import se.roseabrams.footprintdiary.interfaces.Video;

public abstract class YouTubeEvent extends DiaryEntry implements RemoteResource, Video {

    public final YouTubeVideo VIDEO;

    public YouTubeEvent(DiaryDate dd, YouTubeVideo video) {
        super(DiaryEntryCategory.YOUTUBE, dd);
        VIDEO = video;
    }

    @Override
    public String getPathToResource() {
        return VIDEO.getUrl().getPath();
    }

    public URL getUrlOfResource() {
        return VIDEO.getUrl();
    }
}