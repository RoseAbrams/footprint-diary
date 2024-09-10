package se.roseabrams.footprintdiary.entries.youtube;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;

public abstract class YouTubeVideoEvent extends YouTubeEvent implements ContentContainer {

    public final YouTubeVideo VIDEO; // if null, deleted video with no details given

    public YouTubeVideoEvent(DiaryDate dd, YouTubeVideo video) {
        super(dd);
        VIDEO = video;
    }

    @Override
    public Content getContent() {
        return VIDEO;
    }
}