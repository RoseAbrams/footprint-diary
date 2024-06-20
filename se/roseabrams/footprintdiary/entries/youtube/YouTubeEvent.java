package se.roseabrams.footprintdiary.entries.youtube;

import se.roseabrams.footprintdiary.DiaryDate;
import se.roseabrams.footprintdiary.DiaryEntry;
import se.roseabrams.footprintdiary.DiaryEntryCategory;
import se.roseabrams.footprintdiary.common.Content;
import se.roseabrams.footprintdiary.common.ContentContainer;

public abstract class YouTubeEvent extends DiaryEntry implements ContentContainer {

    public final YouTubeVideo VIDEO;

    public YouTubeEvent(DiaryDate dd, YouTubeVideo video) {
        super(DiaryEntryCategory.YOUTUBE, dd);
        VIDEO = video;
    }

    @Override
    public Content getContent() {
        return VIDEO;
    }
}