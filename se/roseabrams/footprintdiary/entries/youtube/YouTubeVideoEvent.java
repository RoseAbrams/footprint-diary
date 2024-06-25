package se.roseabrams.footprintdiary.entries.youtube;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;

public abstract class YouTubeVideoEvent extends YouTubeEvent implements ContentContainer {

    public final YouTubeVideo VIDEO;

    public YouTubeVideoEvent(DiaryDate dd, YouTubeVideo video) {
        super(dd);
        assert video != null;
        VIDEO = video;
    }

    @Override
    public Content getContent() {
        return VIDEO;
    }
}